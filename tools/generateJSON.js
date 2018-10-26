#!/usr/bin/env node

'use strict';


// use sync-mysql in order to use synchronous operations (using async/await) to avoid JavaScript callback nesting
// and to handle queries/DB operations in a procedural manner
const mysql = require('sync-mysql'); // connector to mySQL
const program = require('commander'); //command line parser
const marky = require('marky'); // stopwatch for measuring processing times

const fs = require('fs'); // node.js included file system utils
const sqlConstants = require('./sqlDefs.js'); // generated code

/*
These indexes should be created to increase performance

CREATE INDEX `Table6.object_instanceName_timeStep_index` ON `Table6.object` (instanceName(64), timeStep);
CREATE INDEX `Table2.object_instanceName_timeStep_index` ON `Table2.object` (instanceName(64), timeStep);
 */


(async function () {

	let configFileName = '';
	let dbName = '';

	program
		.version('1.0.0')
		.arguments('<configFile> <dbNameParam>')
		.action(function (configFile,dbNameParam) {
			configFileName = configFile;
            dbName = dbNameParam;
		});

	program.parse(process.argv);

	if (typeof configFileName === 'undefined' || configFileName === '') {
		console.error('no configFile given!');
		process.exit(1);
	}
	console.log('configFile:', configFileName);
    console.log('dbName:', dbName);

	try {

		let config = JSON.parse(fs.readFileSync(__dirname + '/' + configFileName , 'utf8'));

		let params = config.params;
		let dbConfig = config.db;
        dbConfig.database = dbName;

		let path = __dirname + '/' + config.outputDir + '/';

		if (!fs.existsSync(path)){

			fs.mkdirSync(path);

		} else {

			// have to remove previous output files if they exist
			await removeAllFiles(path);

		}

		let connection = new mysql(dbConfig);

		let tables = connection.query('SELECT * FROM IndexTable order by indexId');

		// can't use forEach as it will not await async functions
		// use for loop instead

		for (let table of tables) {

			let result = await getData(connection, table.dataName, params);
			let fileName = path + table.dataName + ".json";
			await fs.appendFileSync(fileName, JSON.stringify(result, null, '\t'));

		}

		console.info("Timing: ");
		console.info(marky.getEntries());

	} catch (err) {

		console.error(err.message);

	} finally {

	}

})();



async function removeAllFiles(directory) {

	try {

		const files = fs.readdirSync(directory);
		const unlinkPromises = files.map(filename => fs.unlinkSync(`${directory}/${filename}`));
		return await Promise.all(unlinkPromises);

	} catch(err) {

		console.log(err);

	}
}

async function getTableName(connection,objTypeName){

	const result = connection.query('SELECT * FROM IndexTable where dataName=?',[objTypeName]);

	if(objTypeName.startsWith("ObjectRoot")){
		return "Table"+result[0].indexId+".object";
	}

	return "Table"+result[0].indexId+".interaction";
}

async function getData(connection,objectTypeName,params){

	let tableName = await getTableName(connection,objectTypeName);
	console.log("Processing: " + objectTypeName + " --- table name: " + tableName);
	let sql = sqlConstants.getQueryString(objectTypeName,tableName,params);
	marky.mark(objectTypeName);
	let result = await connection.query(sql);
	marky.stop(objectTypeName);
	return result;
}
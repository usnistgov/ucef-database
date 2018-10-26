module.exports = {
getQueryString(objectType,tableName,params) {

let whereClause = '';
let andClause = false;
if(params.startStep || params.endStep || params.instanceName) {
whereClause = 'where ';
}

if(params.instanceName){
whereClause += "inst_0.instanceName='" + params.instanceName + "'";
andClause = true;
}

if(params.startStep){
if(andClause){
whereClause += " and ";
}
whereClause += "inst_0.timeStep>=" + params.startStep + " ";
andClause = true;
}

if(params.startStep){
if(andClause){
whereClause += " and ";
}
whereClause += "inst_0.timeStep<=" + params.endStep + " ";
andClause = true;
}

switch (objectType) {
case 'ObjectRoot.FederateObject':
return "select * from\n" + 
"\t(\n" + 
"\tselect distinct\n" + 
"\t	inst_0.instanceName,\n" + 
"\t	inst_0.timeStep,\n" + 
"\t(select CAST(inst_1.attribute AS SIGNED) from `" + tableName + "` as inst_1 where inst_1.description = 'FederateHandle' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as FederateHandle,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'FederateHost' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as FederateHost,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'FederateType' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as FederateType\n" + 
"\tFROM `" + tableName + "` as inst_0 " + whereClause + ") as A\n" + 
"\torder by instanceName,timeStep asc;";


case 'ObjectRoot.Quote':
return "select * from\n" + 
"\t(\n" + 
"\tselect distinct\n" + 
"\t	inst_0.instanceName,\n" + 
"\t	inst_0.timeStep,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'price' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as price,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'quantity' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as quantity,\n" + 
"\t(select CAST(inst_1.attribute AS SIGNED) from `" + tableName + "` as inst_1 where inst_1.description = 'quoteId' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as quoteId,\n" + 
"\t(select CAST(inst_1.attribute AS SIGNED) from `" + tableName + "` as inst_1 where inst_1.description = 'timeReference' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as timeReference,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'type' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as type\n" + 
"\tFROM `" + tableName + "` as inst_0 " + whereClause + ") as A\n" + 
"\torder by instanceName,timeStep asc;";


case 'ObjectRoot.Tender':
return "select * from\n" + 
"\t(\n" + 
"\tselect distinct\n" + 
"\t	inst_0.instanceName,\n" + 
"\t	inst_0.timeStep,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'price' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as price,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'quantity' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as quantity,\n" + 
"\t(select CAST(inst_1.attribute AS SIGNED) from `" + tableName + "` as inst_1 where inst_1.description = 'tenderId' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as tenderId,\n" + 
"\t(select CAST(inst_1.attribute AS SIGNED) from `" + tableName + "` as inst_1 where inst_1.description = 'timeReference' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as timeReference,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'type' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as type\n" + 
"\tFROM `" + tableName + "` as inst_0 " + whereClause + ") as A\n" + 
"\torder by instanceName,timeStep asc;";


case 'ObjectRoot.Transaction':
return "select * from\n" + 
"\t(\n" + 
"\tselect distinct\n" + 
"\t	inst_0.instanceName,\n" + 
"\t	inst_0.timeStep,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'accept' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as accept,\n" + 
"\t(select CAST(inst_1.attribute AS SIGNED) from `" + tableName + "` as inst_1 where inst_1.description = 'tenderId' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as tenderId\n" + 
"\tFROM `" + tableName + "` as inst_0 " + whereClause + ") as A\n" + 
"\torder by instanceName,timeStep asc;";


case 'ObjectRoot.gridVoltageState':
return "select * from\n" + 
"\t(\n" + 
"\tselect distinct\n" + 
"\t	inst_0.instanceName,\n" + 
"\t	inst_0.timeStep,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'grid_Voltage_Imaginary_A' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as grid_Voltage_Imaginary_A,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'grid_Voltage_Imaginary_B' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as grid_Voltage_Imaginary_B,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'grid_Voltage_Imaginary_C' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as grid_Voltage_Imaginary_C,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'grid_Voltage_Real_A' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as grid_Voltage_Real_A,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'grid_Voltage_Real_B' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as grid_Voltage_Real_B,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'grid_Voltage_Real_C' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as grid_Voltage_Real_C\n" + 
"\tFROM `" + tableName + "` as inst_0 " + whereClause + ") as A\n" + 
"\torder by instanceName,timeStep asc;";


case 'ObjectRoot.marketStatus':
return "select * from\n" + 
"\t(\n" + 
"\tselect distinct\n" + 
"\t	inst_0.instanceName,\n" + 
"\t	inst_0.timeStep,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'price' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as price,\n" + 
"\t(select CAST(inst_1.attribute AS SIGNED) from `" + tableName + "` as inst_1 where inst_1.description = 'time' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as time,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'type' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as type\n" + 
"\tFROM `" + tableName + "` as inst_0 " + whereClause + ") as A\n" + 
"\torder by instanceName,timeStep asc;";


case 'ObjectRoot.resourceControl':
return "select * from\n" + 
"\t(\n" + 
"\tselect distinct\n" + 
"\t	inst_0.instanceName,\n" + 
"\t	inst_0.timeStep,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'Resources' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as Resources,\n" + 
"\t(select CAST(inst_1.attribute AS SIGNED) from `" + tableName + "` as inst_1 where inst_1.description = 'activePowerCurve' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as activePowerCurve,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'actualDemand' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as actualDemand,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'adjustedFullDRPower' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as adjustedFullDRPower,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'adjustedNoDRPower' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as adjustedNoDRPower,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'downBeginRamp' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as downBeginRamp,\n" + 
"\t(select CAST(inst_1.attribute AS SIGNED) from `" + tableName + "` as inst_1 where inst_1.description = 'downDuration' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as downDuration,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'downRampToCompletion' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as downRampToCompletion,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'downRate' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as downRate,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'loadStatusType' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as loadStatusType,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'locked' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as locked,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'maximumReactivePower' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as maximumReactivePower,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'maximumRealPower' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as maximumRealPower,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'reactiveDesiredFractionOfFullRatedOutputBegin' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as reactiveDesiredFractionOfFullRatedOutputBegin,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'reactiveDesiredFractionOfFullRatedOutputEnd' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as reactiveDesiredFractionOfFullRatedOutputEnd,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'reactiveRequiredFractionOfFullRatedInputPowerDrawnBegin' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as reactiveRequiredFractionOfFullRatedInputPowerDrawnBegin,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'reactiveRequiredFractionOfFullRatedInputPowerDrawnEnd' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as reactiveRequiredFractionOfFullRatedInputPowerDrawnEnd,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'realDesiredFractionOfFullRatedOutputBegin' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as realDesiredFractionOfFullRatedOutputBegin,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'realDesiredFractionOfFullRatedOutputEnd' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as realDesiredFractionOfFullRatedOutputEnd,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'realRequiredFractionOfFullRatedInputPowerDrawnBegin' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as realRequiredFractionOfFullRatedInputPowerDrawnBegin,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'realRequiredFractionOfFullRatedInputPowerDrawnEnd' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as realRequiredFractionOfFullRatedInputPowerDrawnEnd,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'upBeginRamp' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as upBeginRamp,\n" + 
"\t(select CAST(inst_1.attribute AS SIGNED) from `" + tableName + "` as inst_1 where inst_1.description = 'upDuration' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as upDuration,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'upRampToCompletion' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as upRampToCompletion,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'upRate' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as upRate\n" + 
"\tFROM `" + tableName + "` as inst_0 " + whereClause + ") as A\n" + 
"\torder by instanceName,timeStep asc;";


case 'ObjectRoot.resourcesPhysicalStatus':
return "select * from\n" + 
"\t(\n" + 
"\tselect distinct\n" + 
"\t	inst_0.instanceName,\n" + 
"\t	inst_0.timeStep,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'current_Imaginary_A' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as current_Imaginary_A,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'current_Imaginary_B' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as current_Imaginary_B,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'current_Imaginary_C' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as current_Imaginary_C,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'current_Real_A' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as current_Real_A,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'current_Real_B' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as current_Real_B,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'current_Real_C' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as current_Real_C,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'gridNodeId' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as gridNodeId,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'impedance_Imaginary_A' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as impedance_Imaginary_A,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'impedance_Imaginary_B' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as impedance_Imaginary_B,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'impedance_Imaginary_C' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as impedance_Imaginary_C,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'impedance_Real_A' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as impedance_Real_A,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'impedance_Real_B' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as impedance_Real_B,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'impedance_Real_C' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as impedance_Real_C,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'loadInstanceName' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as loadInstanceName,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'name' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as name,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'phases' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as phases,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'power_Imaginary_A' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as power_Imaginary_A,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'power_Imaginary_B' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as power_Imaginary_B,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'power_Imaginary_C' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as power_Imaginary_C,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'power_Real_A' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as power_Real_A,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'power_Real_B' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as power_Real_B,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'power_Real_C' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as power_Real_C,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'status' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as status,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'type' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as type,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'voltage_Imaginary_A' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as voltage_Imaginary_A,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'voltage_Imaginary_B' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as voltage_Imaginary_B,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'voltage_Imaginary_C' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as voltage_Imaginary_C,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'voltage_Real_A' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as voltage_Real_A,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'voltage_Real_B' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as voltage_Real_B,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'voltage_Real_C' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as voltage_Real_C\n" + 
"\tFROM `" + tableName + "` as inst_0 " + whereClause + ") as A\n" + 
"\torder by instanceName,timeStep asc;";


case 'ObjectRoot.supervisoryControlSignal':
return "select * from\n" + 
"\t(\n" + 
"\tselect distinct\n" + 
"\t	inst_0.instanceName,\n" + 
"\t	inst_0.timeStep,\n" + 
"\t(select CAST(inst_1.attribute AS CHAR) from `" + tableName + "` as inst_1 where inst_1.description = 'localControllerName' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as localControllerName,\n" + 
"\t(select CAST(inst_1.attribute AS DECIMAL(10,3)) from `" + tableName + "` as inst_1 where inst_1.description = 'modulationSignal' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as modulationSignal\n" + 
"\tFROM `" + tableName + "` as inst_0 " + whereClause + ") as A\n" + 
"\torder by instanceName,timeStep asc;";


case 'InteractionRoot.C2WInteractionRoot.ActionBase':
return "select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";

case 'InteractionRoot.C2WInteractionRoot.FederateJoinInteraction':
return "select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";

case 'InteractionRoot.C2WInteractionRoot.FederateResignInteraction':
return "select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";

case 'InteractionRoot.C2WInteractionRoot.OutcomeBase':
return "select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";

case 'InteractionRoot.C2WInteractionRoot.SimLog.HighPrio':
return "select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";

case 'InteractionRoot.C2WInteractionRoot.SimLog.LowPrio':
return "select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";

case 'InteractionRoot.C2WInteractionRoot.SimLog.MediumPrio':
return "select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";

case 'InteractionRoot.C2WInteractionRoot.SimLog.VeryLowPrio':
return "select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";

case 'InteractionRoot.C2WInteractionRoot.SimulationControl.SimEnd':
return "select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";

case 'InteractionRoot.C2WInteractionRoot.SimulationControl.SimPause':
return "select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";

case 'InteractionRoot.C2WInteractionRoot.SimulationControl.SimResume':
return "select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";

case 'InteractionRoot.C2WInteractionRoot.SimTime':
return "select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";

case 'InteractionRoot.C2WInteractionRoot.TMYWeather':
return "select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";

}
}
};

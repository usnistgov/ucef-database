<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:default="http://standards.ieee.org/IEEE1516-2010">

	<xsl:output method="text" encoding="UTF-8" indent="no"/>


	<!--the following is used for development/testing purposes-->
	<!--<xsl:variable name="genRawSQL" select="'true'"/>-->
	<!--otherwise default for normal use is: -->
	<xsl:variable name="genRawSQL" select="'false'"/>


	<xsl:variable name="startOfLine">
		<xsl:choose>
			<xsl:when test="$genRawSQL='true'">

			</xsl:when>
			<xsl:otherwise>
				<xsl:text>"\t</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<xsl:variable name="endOfLine">
		<xsl:choose>
			<xsl:when test="$genRawSQL='true'">
				<xsl:text>&#xD;&#xA;</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>\n" + </xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<xsl:template match="/">

		<xsl:if test="$genRawSQL!='true'">
			<xsl:text>module.exports = {</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>getQueryString(objectType,tableName,params) {</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text></xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>let whereClause = '';</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>let andClause = false;</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>if(params.startStep || params.endStep || params.instanceName) {</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>whereClause = 'where ';</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>}</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text></xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>if(params.instanceName){</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>whereClause += "inst_0.instanceName='" + params.instanceName + "'";</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>andClause = true;</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>}</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text></xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>if(params.startStep){</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>if(andClause){</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>whereClause += " and ";</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>}</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>whereClause += "inst_0.timeStep>=" + params.startStep + " ";</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>andClause = true;</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>}</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text></xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>if(params.startStep){</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>if(andClause){</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>whereClause += " and ";</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>}</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>whereClause += "inst_0.timeStep&lt;=" + params.endStep + " ";</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>andClause = true;</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>}</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text></xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>switch (objectType) {</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
		</xsl:if>

		<xsl:for-each select="//default:objectClass[not(./default:objectClass)]">

			<!--These are also for development/test purposes-->
			<xsl:variable name="tableName"><xsl:value-of select="default:name/text()"/></xsl:variable>
			<!--<xsl:variable name="tableName"><xsl:text>Table6.object</xsl:text></xsl:variable>-->

			<xsl:variable name="className">
				<xsl:for-each select="ancestor::default:objectClass">
					<xsl:if test="default:name/text()!='ObjectRoot'">
						<xsl:text>.</xsl:text>
					</xsl:if>
					<xsl:value-of select="default:name/text()"/>
				</xsl:for-each>
				<xsl:text>.</xsl:text>
				<xsl:value-of select="default:name/text()"/>
			</xsl:variable>

			<xsl:if test="$genRawSQL!='true'">
				<xsl:text>case '</xsl:text><xsl:value-of select="$className"/><xsl:text>':</xsl:text>
				<!--<xsl:text>case '</xsl:text><xsl:value-of select="../default:name/text()"/><xsl:text>.</xsl:text><xsl:value-of select="default:name/text()"/><xsl:text>':</xsl:text>-->
				<xsl:text>&#xD;&#xA;</xsl:text>
				<xsl:text>return "</xsl:text>
			</xsl:if>

			<xsl:if test="$genRawSQL='true'">
				<xsl:text>## Type:</xsl:text><xsl:value-of select="../default:name/text()"/><xsl:text>.</xsl:text><xsl:value-of select="default:name/text()"/><xsl:text>':</xsl:text>
				<xsl:text>&#xD;&#xA;</xsl:text>
			</xsl:if>
				<xsl:text>select * from</xsl:text>
			<xsl:value-of select="$endOfLine"/>
			<xsl:value-of select="$startOfLine"/>
				<xsl:text>(</xsl:text>
			<xsl:value-of select="$endOfLine"/>
			<xsl:value-of select="$startOfLine"/>
				<xsl:text>select distinct</xsl:text>
			<xsl:value-of select="$endOfLine"/>

			<xsl:value-of select="$startOfLine"/>
				<xsl:text>	inst_0.instanceName,</xsl:text>
			<xsl:value-of select="$endOfLine"/>

			<xsl:value-of select="$startOfLine"/>
				<xsl:text>	inst_0.timeStep,</xsl:text>
			<xsl:value-of select="$endOfLine"/>

			<xsl:for-each select="default:attribute">

				<xsl:variable name="cast">
					<xsl:call-template name="dataType">
						<xsl:with-param name="type" select = "default:dataType" />
					</xsl:call-template>
				</xsl:variable>

				<xsl:value-of select="$startOfLine"/>
					<xsl:if test="$genRawSQL='true'">
						<xsl:text>(select CAST(inst_1.attribute AS </xsl:text><xsl:value-of select="$cast"/><xsl:text>) from `</xsl:text><xsl:value-of select="$tableName"/><xsl:text>` as inst_1 where inst_1.description = '</xsl:text><xsl:value-of select="default:name/text()"/><xsl:text>' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as </xsl:text><xsl:value-of select="default:name/text()"/>
					</xsl:if>
					<xsl:if test="$genRawSQL!='true'">
						<xsl:text>(select CAST(inst_1.attribute AS </xsl:text><xsl:value-of select="$cast"/><xsl:text>) from `" + tableName + "` as inst_1 where inst_1.description = '</xsl:text><xsl:value-of select="default:name/text()"/><xsl:text>' and inst_1.instanceName = inst_0.instanceName and inst_1.timeStep = inst_0.timeStep) as </xsl:text><xsl:value-of select="default:name/text()"/>
					</xsl:if>

					<xsl:if test="position()!=last()">
						<xsl:text>,</xsl:text>
					</xsl:if>
				<xsl:value-of select="$endOfLine"/>

			</xsl:for-each>

			<xsl:value-of select="$startOfLine"/>
				<xsl:if test="$genRawSQL='true'">
					<xsl:text>FROM `</xsl:text><xsl:value-of select="$tableName"/><xsl:text>` as inst_0 ) as A</xsl:text>
				</xsl:if>
				<xsl:if test="$genRawSQL!='true'">
					<xsl:text>FROM `" + tableName + "` as inst_0 " + whereClause + ") as A</xsl:text>
				</xsl:if>

			<xsl:value-of select="$endOfLine"/>

			<xsl:if test="$genRawSQL='true'">
				<xsl:value-of select="$startOfLine"/>
					<xsl:text>order by instanceName,timeStep asc;</xsl:text>
				<xsl:value-of select="$endOfLine"/>
			</xsl:if>
			<xsl:if test="$genRawSQL!='true'">
				<xsl:value-of select="$startOfLine"/>
				<xsl:text>order by instanceName,timeStep asc;</xsl:text>
			</xsl:if>

			<xsl:if test="$genRawSQL!='true'">
				<xsl:text>";</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			</xsl:if>
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>&#xD;&#xA;</xsl:text>

		</xsl:for-each>


		<xsl:for-each select="//default:interactionClass[not(./default:interactionClass)]">

			<!--These are also for development/test purposes-->
			<xsl:variable name="tableName"><xsl:value-of select="default:name/text()"/></xsl:variable>
			<!--<xsl:variable name="tableName"><xsl:text>Table6.object</xsl:text></xsl:variable>-->

			<xsl:variable name="className">
				<xsl:for-each select="ancestor::default:interactionClass">
					<xsl:if test="default:name/text()!='InteractionRoot'">
						<xsl:text>.</xsl:text>
					</xsl:if>
					<xsl:value-of select="default:name/text()"/>
				</xsl:for-each>
				<xsl:text>.</xsl:text>
				<xsl:value-of select="default:name/text()"/>
			</xsl:variable>

			<xsl:if test="$genRawSQL!='true'">
				<xsl:text>case '</xsl:text><xsl:value-of select="$className"/><xsl:text>':</xsl:text>
				<xsl:text>&#xD;&#xA;</xsl:text>
				<xsl:text>return "</xsl:text>
			</xsl:if>

			<xsl:if test="$genRawSQL='true'">
				<xsl:text>## Type:</xsl:text><xsl:value-of select="../default:name/text()"/><xsl:text>.</xsl:text><xsl:value-of select="default:name/text()"/><xsl:text>':</xsl:text>
				<xsl:text>&#xD;&#xA;</xsl:text>
			</xsl:if>
			<xsl:if test="$genRawSQL!='true'">
				<xsl:text>select * from `" + tableName + "` as inst_0 " + whereClause + " order by timeStep asc;";</xsl:text>
				<xsl:text>&#xD;&#xA;</xsl:text>
			</xsl:if>
			<xsl:if test="$genRawSQL='true'">
				<xsl:text>select * from `</xsl:text><xsl:value-of select="$tableName"/><xsl:text>` order by timeStep asc;</xsl:text>
			</xsl:if>

			<xsl:text>&#xD;&#xA;</xsl:text>

		</xsl:for-each>


		<xsl:if test="$genRawSQL!='true'">
			<xsl:text>}</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>}</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>};</xsl:text><xsl:text>&#xD;&#xA;</xsl:text>
		</xsl:if>
	</xsl:template>


	<xsl:template name="dataType">
		<xsl:param name="type"/>
		<xsl:choose>
			<xsl:when test="$type='float'">
				<xsl:text>DECIMAL(10,3)</xsl:text>
			</xsl:when>
			<xsl:when test="$type='String'">
				<xsl:text>CHAR</xsl:text>
			</xsl:when>
			<xsl:when test="$type='Boolean'">
				<xsl:text>BINARY</xsl:text>
			</xsl:when>
			<xsl:when test="$type='byte'">
				<xsl:text>UNSIGNED</xsl:text>
			</xsl:when>
			<xsl:when test="$type='char'">
				<xsl:text>CHAR</xsl:text>
			</xsl:when>
			<xsl:when test="$type='double'">
				<xsl:text>DECIMAL(10,3)</xsl:text>
			</xsl:when>
			<xsl:when test="$type='int'">
				<xsl:text>SIGNED</xsl:text>
			</xsl:when>
			<xsl:when test="$type='long'">
				<xsl:text>SIGNED</xsl:text>
			</xsl:when>
			<xsl:when test="$type='short'">
				<xsl:text>SIGNED</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>CHAR</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>


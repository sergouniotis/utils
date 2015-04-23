<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns="urn:eu.emsa.ssn"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:epc="http://www.iso.org/28005-2"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:math="http://exslt.org/math"
	xmlns:string="http://exslt.org/strings" xmlns:dates-and-times="http://exslt.org/dates-and-times"
	extension-element-prefixes="dates-and-times math string">

	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" omit-xml-declaration="yes" />

	<xsl:template match="/">
		<MS2SSN_PortPlus_Not>
			<xsl:apply-templates select="//epc:EPCMessage/epc:EPCMessageHeader" />
			<xsl:apply-templates select="//epc:EPCMessage/epc:EPCRequestBody" />
		</MS2SSN_PortPlus_Not>
	</xsl:template>


	<xsl:template match="//epc:EPCMessage/epc:EPCMessageHeader">
		<Header>
			<xsl:attribute name="MSRefId">
				<xsl:text>_MSRefId_</xsl:text>
        	</xsl:attribute>
			<xsl:attribute name="TestId">
				<xsl:value-of select="1234" />
        	</xsl:attribute>
			<xsl:attribute name="Version">
				<xsl:text>3.0</xsl:text>
        	</xsl:attribute>
			<xsl:attribute name="SentAt">
				<xsl:value-of select="dates-and-times:dateTime()" />
        	</xsl:attribute>
			<xsl:attribute name="From">
<!-- 				<xsl:value-of select="epc:SenderId" /> -->
					<xsl:text>_SENDER_</xsl:text>
        	</xsl:attribute>
			<xsl:attribute name="To">
				<xsl:text>SafeSeaNet</xsl:text>
        	</xsl:attribute>
		</Header>
	</xsl:template>


	<xsl:template match="//epc:EPCMessage/epc:EPCRequestBody">
		<Body>
			<NotificationStatus>
				<xsl:attribute name="UpdateStatus">
						  <xsl:text>_UpdateStatus_</xsl:text>
			        </xsl:attribute>
			      <xsl:if test="'_UpdateStatus_'= 'U'">
					<UpdateNotifications>
						<xsl:attribute name="UpdateMSRefId">
							  <xsl:value-of
							select="//epc:EPCMessage/epc:EPCMessageHeader/epc:ShipMessageId" />
				        	</xsl:attribute>
					</UpdateNotifications>
				</xsl:if>			        
			</NotificationStatus>

			<Notification>
				<xsl:apply-templates
					select="//epc:EPCMessage/epc:EPCRequestBody/epc:ShipID" />
				<VoyageInformation>

					<xsl:attribute name="ShipCallId">
								<xsl:text>_ShipCallId_</xsl:text>
					</xsl:attribute>

					<xsl:if
						test="//epc:EPCMessage/epc:EPCMessageHeader/epc:ArrivalDeparture = 'Arrival'">
						<xsl:attribute name="PortOfCall">
							<xsl:value-of
							select="concat(//epc:EPCMessage/epc:EPCRequestBody/epc:PortOfArrival/epc:CountryCode,//epc:EPCMessage/epc:EPCRequestBody/epc:PortOfArrival/epc:UNLoCode)" />
						</xsl:attribute>

						<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:LastPortOfCall">
							<xsl:attribute name="LastPort">
								<xsl:value-of
								select="concat(//epc:EPCMessage/epc:EPCRequestBody/epc:LastPortOfCall/epc:CountryCode ,//epc:EPCMessage/epc:EPCRequestBody/epc:LastPortOfCall/epc:UNLoCode)" />
							</xsl:attribute>
						</xsl:if>

						<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:ETDFromLastPort">
							<xsl:attribute name="ETDFromLastPort">
								<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:ETDFromLastPort" />				
	        				</xsl:attribute>
						</xsl:if>
					</xsl:if>

					<xsl:if
						test="//epc:EPCMessage/epc:EPCMessageHeader/epc:ArrivalDeparture = 'Departure'">
						<xsl:attribute name="PortOfCall">
							<xsl:value-of
							select="concat(//epc:EPCMessage/epc:EPCRequestBody/epc:PortOfDeparture/epc:CountryCode,//epc:EPCMessage/epc:EPCRequestBody/epc:PortOfDeparture/epc:UNLoCode)" />
						</xsl:attribute>

						<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:NextPortOfCall">
							<xsl:attribute name="NextPort">
							<xsl:value-of
								select="concat(//epc:EPCMessage/epc:EPCRequestBody/epc:NextPortOfCall/epc:CountryCode,//epc:EPCMessage/epc:EPCRequestBody/epc:NextPortOfCall/epc:UNLoCode)" />				
	        			</xsl:attribute>
						</xsl:if>

						<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:ETAToNextPort">
							<xsl:attribute name="ETAToNextPort">
							<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:ETAToNextPort" />				
       					</xsl:attribute>
						</xsl:if>
					</xsl:if>

					<xsl:if
						test="//epc:EPCMessage/epc:EPCRequestBody/epc:PositionInPortOfCall">
						<xsl:attribute name="PositionInPortOfCall">
							<xsl:value-of
							select="//epc:EPCMessage/epc:EPCRequestBody/epc:PositionInPortOfCall" />				
       					</xsl:attribute>
					</xsl:if>

					<xsl:if
						test="//epc:EPCMessage/epc:EPCRequestBody/epc:PortOfArrival/epc:GISISCode">
						<xsl:attribute name="PortFacility">
							<xsl:value-of
							select="//epc:EPCMessage/epc:EPCRequestBody/epc:PortOfArrival/epc:GISISCode" />				
       					</xsl:attribute>
					</xsl:if>



					<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:ETA">
						<xsl:attribute name="ETAToPortOfCall">
							<xsl:value-of select="epc:ETA" />				
        				</xsl:attribute>
					</xsl:if>

					<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:ETD">
						<xsl:attribute name="ETDFromPortOfCall">
							<xsl:value-of select="//epc:EPCMessage/epc:EPCRequestBody/epc:ETD" />				
	        			</xsl:attribute>
					</xsl:if>

					<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:ETD">
						<xsl:attribute name="ETDFromPortOfCall">
							<xsl:value-of select="//epc:EPCMessage/epc:EPCRequestBody/epc:ETD" />				
	        			</xsl:attribute>
					</xsl:if>

					<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoOverview">
						<xsl:attribute name="BriefCargoDescription">
							<xsl:value-of
							select="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoOverview" />
					</xsl:attribute>
					</xsl:if>

					<xsl:for-each select="//epc:EPCMessage/epc:EPCRequestBody/epc:CallPurpose">
						<PurposeOfCall>
							<xsl:attribute name="CallPurposeCode">
								<xsl:value-of select="epc:CallPurposeCode" />
							</xsl:attribute>
						</PurposeOfCall>
					</xsl:for-each>
				</VoyageInformation>

				<VesselDetails>
					<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:GrossTonnage">
						<xsl:attribute name="GrossTonnage">
							<xsl:value-of
							select="//epc:EPCMessage/epc:EPCRequestBody/epc:GrossTonnage" />
					</xsl:attribute>
					</xsl:if>

					<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:ShipTypeContent">
						<xsl:attribute name="ShipType">
							<xsl:value-of
							select="//epc:EPCMessage/epc:EPCRequestBody/epc:ShipTypeContent" />
							</xsl:attribute>
					</xsl:if>

					<xsl:for-each
						select="//epc:EPCMessage/epc:EPCRequestBody/epc:InmarsatCallNumber">
						<InmarsatCallNumber>
							<xsl:attribute name="Inmarsat">
								<xsl:value-of select="epc:Inmarsat" />
								</xsl:attribute>
						</InmarsatCallNumber>
					</xsl:for-each>

					<xsl:if
						test="//epc:EPCMessage/epc:EPCRequestBody/epc:RegistryCertificate">
						<CertificateOfRegistry>
							<xsl:if
								test="//epc:EPCMessage/epc:EPCRequestBody/epc:RegistryCertificate">
								<xsl:attribute name="IssueDate">
								<xsl:value-of
									select="//epc:EPCMessage/epc:EPCRequestBody/epc:RegistryCertificate/epc:IssueDate" />
							</xsl:attribute>

								<xsl:attribute name="CertificateNumber">
								<xsl:value-of
									select="//epc:EPCMessage/epc:EPCRequestBody/epc:RegistryCertificate/epc:Number" />
							</xsl:attribute>
							</xsl:if>
							<xsl:if
								test="//epc:EPCMessage/epc:EPCRequestBody/epc:RegistryCertificate">
								<PortOfRegistry>
									<xsl:if
										test="//epc:EPCMessage/epc:EPCRequestBody/epc:RegistryCertificate/epc:IssueLocation/epc:CountryCode">
										<xsl:attribute name="LoCode">
								<xsl:value-of
											select="concat(//epc:EPCMessage/epc:EPCRequestBody/epc:RegistryCertificate/epc:IssueLocation/epc:CountryCode , //epc:EPCMessage/epc:EPCRequestBody/epc:RegistryCertificate/epc:IssueLocation/epc:UNLoCode)" />
							</xsl:attribute>
									</xsl:if>
									<xsl:if
										test="//epc:EPCMessage/epc:EPCRequestBody/epc:RegistryCertificate/epc:IssueLocation/epc:Name">
										<xsl:attribute name="LocationName">
								<xsl:value-of
											select="//epc:EPCMessage/epc:EPCRequestBody/epc:RegistryCertificate/epc:IssueLocation/epc:Name" />
							</xsl:attribute>
									</xsl:if>
								</PortOfRegistry>
							</xsl:if>
						</CertificateOfRegistry>
					</xsl:if>

					<xsl:if
						test="//epc:EPCMessage/epc:EPCRequestBody/epc:Company/epc:Contact/epc:Company">
						<Company>
							<xsl:if
								test="//epc:EPCMessage/epc:EPCRequestBody/epc:Company/epc:Contact/epc:Company">
								<xsl:attribute name="CompanyName">
								<xsl:value-of
									select="//epc:EPCMessage/epc:EPCRequestBody/epc:Company/epc:Contact/epc:Company" />
							</xsl:attribute>
							</xsl:if>

							<xsl:if
								test="//epc:EPCMessage/epc:EPCRequestBody/epc:Company/epc:IMOCompanyId">
								<xsl:attribute name="IMOCompanyNr">
								<xsl:value-of
									select="//epc:EPCMessage/epc:EPCRequestBody/epc:Company/epc:IMOCompanyId" />
							</xsl:attribute>
							</xsl:if>
						</Company>
					</xsl:if>
				</VesselDetails>

				<xsl:if
					test="normalize-space(//epc:EPCMessage/epc:EPCRequestBody/epc:PossibleAnchorage)!='' or normalize-space(//epc:EPCMessage/epc:EPCRequestBody/epc:PlannedOperations)!=''  or normalize-space(//epc:EPCMessage/epc:EPCRequestBody/epc:PlannedWorks)!=''  ">
					<PreArrival3DaysNotificationDetails>
						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:PossibleAnchorage">
							<xsl:attribute name="PossibleAnchorage">
							<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:PossibleAnchorage" />				
        				</xsl:attribute>
						</xsl:if>

						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:PlannedOperations">
							<xsl:attribute name="PlannedOperations">
							<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:PlannedOperations" />				
        				</xsl:attribute>
						</xsl:if>

						<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:PlannedWorks">
							<xsl:attribute name="PlannedWorks">
							<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:PlannedWorks" />				
        				</xsl:attribute>
						</xsl:if>

						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:TankerHullConfiguration">
							<xsl:attribute name="ShipConfiguration">
							<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:TankerHullConfiguration" />				
        				</xsl:attribute>
						</xsl:if>

						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:VolumeAndNatureOfCargo">
							<xsl:attribute name="CargoVolumeNature">
							<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:VolumeAndNatureOfCargo" />				
        				</xsl:attribute>
						</xsl:if>

						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:ConditionOfCargoAndBallastTanks">
							<xsl:attribute name="ConditionCargoBallastTanks">
							<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:ConditionOfCargoAndBallastTanks" />				
        				</xsl:attribute>
						</xsl:if>

						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:TankerHullConfiguration">
							<xsl:attribute name="ShipConfiguration">
							<xsl:value-of select="epc:TankerHullConfiguration" />				
        				</xsl:attribute>
						</xsl:if>
					</PreArrival3DaysNotificationDetails>

				</xsl:if>

				<!-- if dpg is defined in arrival notification then POBVoyageTowardsPortOfCall 
					is mandatory -->
				<xsl:if
					test="//epc:EPCMessage/epc:EPCMessageHeader/epc:ArrivalDeparture = 'Arrival'">
					<PreArrival24HoursNotificationDetails>
						<xsl:attribute name="POBVoyageTowardsPortOfCall">
						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:NumberOfPersonsOnBoard > 0">
							<xsl:value-of
							select="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:NumberOfPersonsOnBoard" />	
						</xsl:if>
						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:NumberOfPersonsOnBoard = 0">
							<xsl:text>99999</xsl:text>
						</xsl:if>
						<xsl:if
							test="not (//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:NumberOfPersonsOnBoard)">
							<xsl:text>99999</xsl:text>
						</xsl:if>
        						
						</xsl:attribute>
					</PreArrival24HoursNotificationDetails>
				</xsl:if>


				<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:ATA">
					<ArrivalNotificationDetails>
						<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/Anchorage">
							<xsl:attribute name="Anchorage">
								<xsl:value-of select="//epc:EPCMessage/epc:EPCRequestBody/Anchorage" />				
	        				</xsl:attribute>
						</xsl:if>
						<xsl:attribute name="ATAPortOfCall">
							<xsl:value-of select="//epc:EPCMessage/epc:EPCRequestBody/epc:ATA" />				
	       				</xsl:attribute>
					</ArrivalNotificationDetails>
				</xsl:if>

				<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:ATD">
					<DepartureNotificationDetails>
						<xsl:attribute name="ATDPortOfCall">
							<xsl:value-of select="//epc:EPCMessage/epc:EPCRequestBody/epc:ATD" />				
	       				</xsl:attribute>
	       				

					<xsl:if
						test="//epc:EPCMessage/epc:EPCMessageHeader/epc:ArrivalDeparture = 'Departure' and //epc:EPCMessage/epc:EPCRequestBody/epc:ConfirmDPGListOnBoard/epc:ConfirmDPGOnBoard">
							<xsl:attribute name="POBVoyageTowardsNextPort">
								<xsl:if
									test="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:NumberOfPersonsOnBoard > -1">
									<xsl:value-of
									select="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:NumberOfPersonsOnBoard" />	
								</xsl:if>
								<xsl:if
									test="not (//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:NumberOfPersonsOnBoard)">
									<xsl:text>99999</xsl:text>
								</xsl:if>
							</xsl:attribute>
					</xsl:if>	       				
	       				
					</DepartureNotificationDetails>
				</xsl:if>

				<!-- if is departure and dpg has been defined then hazmat eu element 
					is created -->
				<xsl:if
					test="//epc:EPCMessage/epc:EPCMessageHeader/epc:ArrivalDeparture = 'Departure' and //epc:EPCMessage/epc:EPCRequestBody/epc:ConfirmDPGListOnBoard/epc:ConfirmDPGOnBoard">
					<HazmatNotificationInfoEUDepartures>
						<HazmatCargoInformation>
							<xsl:attribute name="HazmatOnBoardYorN">
								<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:ConfirmDPGListOnBoard/epc:ConfirmDPGOnBoard" />				
	       					</xsl:attribute>
							<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:INFClassContent">
								<xsl:attribute name="INFShipClass">
									<xsl:value-of
									select="//epc:EPCMessage/epc:EPCRequestBody/epc:INFClassContent" />
									</xsl:attribute>
							</xsl:if>
							<xsl:if
								test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoData/epc:Consignment/epc:CargoItem/epc:SpecialCargoDetails/epc:DGSafetyDataSheet/epc:DGClassification">
								<DG>
									<xsl:if
										test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoData/epc:Consignment/epc:CargoItem/epc:SpecialCargoDetails/epc:DGSafetyDataSheet/epc:DGClassification">
										<xsl:attribute name="DGClassification">
									<xsl:value-of
											select="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoData/epc:Consignment/epc:CargoItem/epc:SpecialCargoDetails/epc:DGSafetyDataSheet/epc:DGClassification" />
									</xsl:attribute>
									</xsl:if>
								</DG>
							</xsl:if>
						</HazmatCargoInformation>


						<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest">
							<CargoManifest>
								<xsl:if
									test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:ContactNumbers/epc:BusinessTelephone">
									<ContactDetails>
										<xsl:if
											test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:Person/epc:FamilyName">
											<xsl:attribute name="LastName">
													<xsl:value-of
												select="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:Person/epc:FamilyName" />				
	       										</xsl:attribute>
										</xsl:if>
										<xsl:if
											test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:Person/epc:GivenName">
											<xsl:attribute name="FirstName">
													<xsl:value-of
												select="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:Person/epc:GivenName" />				
	       									</xsl:attribute>
										</xsl:if>

										<xsl:attribute name="Phone">
											<xsl:value-of
											select="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:ContactNumbers/epc:BusinessTelephone" />				
	       								</xsl:attribute>

										<xsl:if
											test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:ContactNumbers/epc:Telefax">
											<xsl:attribute name="Fax">
												<xsl:value-of
												select="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:ContactNumbers/epc:Telefax" />				
	       									</xsl:attribute>
										</xsl:if>
										<xsl:if
											test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:ContactNumbers/epc:Email">
											<xsl:attribute name="Email">
								<xsl:value-of
												select="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:ContactNumbers/epc:Email" />				
	       					</xsl:attribute>
										</xsl:if>
										<xsl:if
											test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Location">
											<xsl:attribute name="LoCode">
													<xsl:value-of
												select="concat(//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Location/epc:CountryCode,//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Location/epc:UNLoCode)" />
	       									</xsl:attribute>
										</xsl:if>
									</ContactDetails>
								</xsl:if>
							</CargoManifest>
						</xsl:if>

					</HazmatNotificationInfoEUDepartures>
				</xsl:if>

				<!-- if is arrival and dpg information has been defined then non hazmat 
					eu element is being created -->
				<xsl:if
					test="//epc:EPCMessage/epc:EPCMessageHeader/epc:ArrivalDeparture = 'Arrival' and //epc:EPCMessage/epc:EPCRequestBody/epc:ConfirmDPGListOnBoard/epc:ConfirmDPGOnBoard">
					<HazmatNotificationInfoNonEUDepartures>
						<HazmatCargoInformation>
							<xsl:attribute name="HazmatOnBoardYorN">
								<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:ConfirmDPGListOnBoard/epc:ConfirmDPGOnBoard" />				
	       					</xsl:attribute>
							<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:INFClassContent">
								<xsl:attribute name="INFShipClass">
									<xsl:value-of
									select="//epc:EPCMessage/epc:EPCRequestBody/epc:INFClassContent" />
									</xsl:attribute>
							</xsl:if>
							<xsl:if
								test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoData/epc:Consignment/epc:CargoItem/epc:SpecialCargoDetails/epc:DGSafetyDataSheet/epc:DGClassification">
								<DG>
									<xsl:if
										test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoData/epc:Consignment/epc:CargoItem/epc:SpecialCargoDetails/epc:DGSafetyDataSheet/epc:DGClassification">
										<xsl:attribute name="DGClassification">
									<xsl:value-of
											select="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoData/epc:Consignment/epc:CargoItem/epc:SpecialCargoDetails/epc:DGSafetyDataSheet/epc:DGClassification" />
									</xsl:attribute>
									</xsl:if>
								</DG>
							</xsl:if>
						</HazmatCargoInformation>

						<xsl:if test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest">
							<CargoManifest>
								<xsl:if
									test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:ContactNumbers/epc:BusinessTelephone">
									<ContactDetails>
										<xsl:if
											test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:Person/epc:FamilyName">
											<xsl:attribute name="LastName">
													<xsl:value-of
												select="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:Person/epc:FamilyName" />				
	       										</xsl:attribute>
										</xsl:if>
										<xsl:if
											test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:Person/epc:GivenName">
											<xsl:attribute name="FirstName">
													<xsl:value-of
												select="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:Person/epc:GivenName" />				
	       									</xsl:attribute>
										</xsl:if>
										<xsl:if
											test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:ContactNumbers/epc:BusinessTelephone">

											<xsl:attribute name="Phone">
												<xsl:value-of
												select="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:ContactNumbers/epc:BusinessTelephone" />				
	       									</xsl:attribute>

											<xsl:if
												test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:ContactNumbers/epc:Telefax">
												<xsl:attribute name="Fax">
								<xsl:value-of
													select="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:ContactNumbers/epc:Telefax" />				
	       					</xsl:attribute>
											</xsl:if>
											<xsl:if
												test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:ContactNumbers/epc:Email">
												<xsl:attribute name="Email">	<xsl:value-of
													select="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Contact/epc:ContactNumbers/epc:Email" /></xsl:attribute>
											</xsl:if>
											<xsl:if
												test="//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Location">
												<xsl:attribute name="LoCode">
													<xsl:value-of
													select="concat(//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Location/epc:CountryCode,//epc:EPCMessage/epc:EPCRequestBody/epc:CargoManifest/epc:Location/epc:UNLoCode)" />	
												</xsl:attribute>
											</xsl:if>
										</xsl:if>
									</ContactDetails>
								</xsl:if>
							</CargoManifest>
						</xsl:if>
					</HazmatNotificationInfoNonEUDepartures>
				</xsl:if>

				<xsl:if
					test="//epc:EPCMessage/epc:EPCRequestBody/epc:WasteInformation/epc:WasteDeliveryStatus">
					<WasteNotification>

						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:WasteInformation/epc:LastPortDelivered">
							<xsl:attribute name="LastPortDelivered">
								<xsl:value-of
								select="concat(//epc:EPCMessage/epc:EPCRequestBody/epc:WasteInformation/epc:LastPortDelivered/epc:CountryCode ,//epc:EPCMessage/epc:EPCRequestBody/epc:WasteInformation/epc:LastPortDelivered/epc:UNLoCode)" />				
							</xsl:attribute>
						</xsl:if>

						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:WasteInformation/epc:LastPortDeliveredDate">
							<xsl:attribute name="LastPortDeliveredDate">
									<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:WasteInformation/epc:LastPortDeliveredDate" />				
			       				</xsl:attribute>
						</xsl:if>

						<xsl:attribute name="WasteDeliveryStatus">
							<xsl:value-of
							select="//epc:EPCMessage/epc:EPCRequestBody/epc:WasteInformation/epc:WasteDeliveryStatus" />				
	       				</xsl:attribute>

					</WasteNotification>
				</xsl:if>


				<xsl:if
					test="//epc:EPCMessage/epc:EPCRequestBody/epc:CurrentShipSecurityLevel">
					<SecurityNotification>
						<xsl:attribute name="CurrentSecurityLevel">
										<xsl:value-of
							select="//epc:EPCMessage/epc:EPCRequestBody/epc:CurrentShipSecurityLevel" />
						</xsl:attribute>
						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:Agent/epc:Company">
							<AgentInPortAtArrival>
								<xsl:if
									test="//epc:EPCMessage/epc:EPCRequestBody/epc:Agent/epc:Company">
									<xsl:attribute name="AgentName">
									<xsl:value-of
										select="//epc:EPCMessage/epc:EPCRequestBody/epc:Agent/epc:Company" />
								
								</xsl:attribute>
								</xsl:if>

								<xsl:if
									test="//epc:EPCMessage/epc:EPCRequestBody/epc:Agent/epc:ContactNumbers/epc:BusinessTelephone">
									<xsl:attribute name="Phone">
									<xsl:value-of
										select="//epc:EPCMessage/epc:EPCRequestBody/epc:Agent/epc:ContactNumbers/epc:BusinessTelephone" />
								
								</xsl:attribute>
								</xsl:if>

								<xsl:if
									test="//epc:EPCMessage/epc:EPCRequestBody/epc:Agent/epc:ContactNumbers/epc:Telefax">
									<xsl:attribute name="Fax">
									<xsl:value-of
										select="//epc:EPCMessage/epc:EPCRequestBody/epc:Agent/epc:ContactNumbers/epc:Telefax" />
								</xsl:attribute>
								</xsl:if>

								<xsl:if
									test="//epc:EPCMessage/epc:EPCRequestBody/epc:Agent/epc:ContactNumbers/epc:EMail">
									<xsl:attribute name="EMail">
									<xsl:value-of
										select="//epc:EPCMessage/epc:EPCRequestBody/epc:Agent/epc:ContactNumbers/epc:EMail" />
								
								</xsl:attribute>
								</xsl:if>

							</AgentInPortAtArrival>
						</xsl:if>
					</SecurityNotification>
				</xsl:if>

				<xsl:if
					test="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:NumberOfPersonsOnBoard > 0 and ( //epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:Crew > 0 or //epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:Passengers > 0 ) and //epc:EPCMessage/epc:EPCMessageHeader/epc:ArrivalDeparture = 'Arrival'">
					<CrewAndPaxNotificationOnArrival>
						<xsl:attribute name="CrewAndPaxYorN">
						<xsl:text>Y</xsl:text>
					</xsl:attribute>

						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:Crew">
							<xsl:attribute name="NumberOfCrew">
							<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:Crew" />
						
							</xsl:attribute>
						</xsl:if>

						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:Passengers">
							<xsl:attribute name="NumberOfPassengers">
							<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:Passengers" />
						</xsl:attribute>
						</xsl:if>

						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:Stowaways">
						<xsl:attribute name="Stowaways">
							<xsl:value-of
							select="//epc:EPCMessage/epc:EPCRequestBody/epc:Stowaways" />
						</xsl:attribute>
						</xsl:if>

						<xsl:for-each
							select="//epc:EPCMessage/epc:EPCRequestBody/epc:CruiseShipItinerary">
							<IteneraryOfCruiseShip>
								<xsl:attribute name="Port">
								<xsl:value-of select="epc:Port" />
							</xsl:attribute>

								<xsl:attribute name="DateOfArrival">
								<xsl:value-of select="epc:ExpectedDateTimeOfArrival" />
							</xsl:attribute>
							</IteneraryOfCruiseShip>
						</xsl:for-each>
					</CrewAndPaxNotificationOnArrival>
				</xsl:if>

				<xsl:if
					test="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:NumberOfPersonsOnBoard > 0 and //epc:EPCMessage/epc:EPCMessageHeader/epc:ArrivalDeparture = 'Departure'">
					<CrewAndPaxNotificationOnDeparture>
						<xsl:attribute name="CrewAndPaxYorN">
						<xsl:text>Y</xsl:text>
					</xsl:attribute>

						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:Crew">
							<xsl:attribute name="NumberOfCrew">
							<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:Crew" />
						
						</xsl:attribute>
						</xsl:if>

						<xsl:if
							test="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:Passengers">
							<xsl:attribute name="NumberOfPassengers">
							<xsl:value-of
								select="//epc:EPCMessage/epc:EPCRequestBody/epc:PersonsOnBoard/epc:Passengers" />
						</xsl:attribute>
						</xsl:if>

						<xsl:attribute name="Stowaways">
						<xsl:value-of select="//epc:EPCMessage/epc:EPCRequestBody/epc:Stowaways" />
					</xsl:attribute>

						<xsl:for-each
							select="//epc:EPCMessage/epc:EPCRequestBody/epc:CruiseShipItinerary">
							<IteneraryOfCruiseShip>
								<xsl:attribute name="Port">
								<xsl:value-of select="epc:Port" />
							</xsl:attribute>

								<xsl:attribute name="DateOfArrival">
								<xsl:value-of select="epc:ExpectedDateTimeOfArrival" />
							</xsl:attribute>
							</IteneraryOfCruiseShip>
						</xsl:for-each>
					</CrewAndPaxNotificationOnDeparture>
				</xsl:if>
			</Notification>
		</Body>
	</xsl:template>

	<xsl:template match="//epc:EPCMessage/epc:EPCRequestBody/epc:ShipID">
		<VesselIdentification>
			<xsl:attribute name="IMONumber">
				<xsl:value-of select="epc:IMONumber" />				
        	</xsl:attribute>
			<xsl:attribute name="MMSINumber">
				<xsl:value-of select="epc:MMSINumber" />				
        	</xsl:attribute>
			<xsl:attribute name="ShipName">
				<xsl:value-of select="epc:ShipName" />				
        	</xsl:attribute>
			<xsl:attribute name="CallSign">
				<xsl:value-of select="epc:CallSign" />				
        	</xsl:attribute>
		</VesselIdentification>
	</xsl:template>

</xsl:stylesheet>
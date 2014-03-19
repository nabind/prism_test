		<c:choose>
			<c:when test="${fn:endsWith(testAdministrationText, '2010')}">
				<c:if test="${fn:startsWith(testAdministrationText, 'ISTEP')}">
					<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
					<a class="button" id="grt2010" href="staticfiles/ISTEP S2009-10 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
					<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File</a>
					</div>
				</c:if>
				<c:if test="${fn:startsWith(testAdministrationText, 'IMAST')}">
					<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
					<a class="button" id="grt2010" href="staticfiles/IMAST S2009-10 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2009-10 GRT File Record Layout</a>
					<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File</a>
					</div>
				</c:if>
			</c:when>
			<c:when test="${fn:endsWith(testAdministrationText, '2011')}">
				<c:if test="${fn:startsWith(testAdministrationText, 'ISTEP')}">
					<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
					<a class="button" id="grt2010" href="staticfiles/ISTEP S2010-11 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2010-11 GRT File Record Layout</a>
					<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File</a>
					</div>
				</c:if>
				<c:if test="${fn:startsWith(testAdministrationText, 'IMAST')}">
					<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
					<a class="button" id="grt2010" href="staticfiles/IMAST S2010-11 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2010-11 GRT File Record Layout</a>
					<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File</a>
					</div>
				</c:if>
			</c:when>
			<c:when test="${fn:endsWith(testAdministrationText, '2012')}">
				<c:if test="${fn:startsWith(testAdministrationText, 'ISTEP')}">
					<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
					<a class="button" id="grt2012" href="staticfiles/ISTEP S2011-12 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2011-12 GRT File Record Layout</a>
					<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File</a>
					</div>
				</c:if>
				<c:if test="${fn:startsWith(testAdministrationText, 'IMAST')}">
					<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
					<a class="button" id="grt2010" href="staticfiles/IMAST S2011-12 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2011-12 GRT File Record Layout</a>
					<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File</a>
					</div>
				</c:if>
				<c:if test="${fn:startsWith(testAdministrationText, 'IREAD')}">
					<c:if test="${fn:contains(testAdministrationText, 'Spring')}">
						<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
						<a class="button" id="grt2010" href="staticfiles/IREAD-3 S2011-12 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2011-12 GRT File Record Layout</a>
						<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File</a>
						</div>
					</c:if>
					<c:if test="${fn:contains(testAdministrationText, 'Summer')}">
						<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
						<a class="button" id="grt2010" href="staticfiles/IREAD-3 R2011-12 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2011-12 GRT File Record Layout</a>
						<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File</a>
						</div>
					</c:if>
				</c:if>
			</c:when>
			<c:when test="${fn:endsWith(testAdministrationText, '2013')}">
				<c:if test="${fn:startsWith(testAdministrationText, 'ISTEP')}">
					<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
					<a class="button" id="grt2013Istep" href="staticfiles/ISTEP S2012-13 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2012-13 GRT File Record Layout</a>
					<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File</a>
					</div>
				</c:if>
				<c:if test="${fn:startsWith(testAdministrationText, 'IMAST')}">
					<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
					<a class="button" id="grt2013Imast" href="staticfiles/IMAST S2012-13 GR 3-8 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2012-13 GRT File Record Layout</a>
					<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File</a>
					</div>
				</c:if>
				<c:if test="${fn:startsWith(testAdministrationText, 'IREAD')}">
					<c:if test="${fn:contains(testAdministrationText, 'Spring')}">
						<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
						<a class="button" id="grt2013IreadSpring" href="staticfiles/IREAD-3 S2012-13 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2012-13 GRT File Record Layout</a>
						<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File</a>
						</div>
					</c:if>
					<c:if test="${fn:contains(testAdministrationText, 'Summer')}">
						<div id="grtDiv" class="columns accordion with-padding" style="margin-bottom: 0">
						<a class="button" id="grt2013IreadSummer" href="staticfiles/IREAD-3 R2012-13 GRT Corp Version.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2012-13 GRT File Record Layout</a>
						<a class="button float-right" id="downloadGRTFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate GRT File</a>
						</div>
					</c:if>
				</c:if>
			</c:when>
			<c:otherwise>
				<!-- Code When GRT File and GRT Layout Are Not Available -->
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${(fn:startsWith(testAdministrationText, 'ISTEP+')) && (fn:endsWith(testAdministrationText, currentAdminYear))}">
				<div id="icDiv" class="columns accordion with-padding" style="margin-bottom: 0">
				<a class="button" id="ic2013" href="staticfiles/S2012-13 Invitation Code Layout.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2012-13 Invitation Code File Record Layout</a>
				<a class="button float-right" id="downloadICFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate IC File</a>
				</div>
			</c:when>
			<c:when test="${(fn:startsWith(testAdministrationText, 'ISTEP+')) && (fn:endsWith(testAdministrationText, lastAdminYear))}">
				<div id="icDiv" class="columns accordion with-padding" style="margin-bottom: 0">
				<a class="button" id="ic2012" href="staticfiles/S2011-12 Invitation Code Layout.xls"><span class="button-icon icon-download blue-gradient report-btn">XLS</span>2011-12 Invitation Code File Record Layout</a>
				<a class="button float-right" id="downloadICFile" style="cursor: pointer;"><span class="button-icon icon-download blue-gradient report-btn">DAT</span> Generate IC File</a>
				</div>
			</c:when>
			<c:otherwise>
				<!-- Code When IC File and Layout Are Not Available -->
			</c:otherwise>
		</c:choose>
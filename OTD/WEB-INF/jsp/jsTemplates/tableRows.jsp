<%--
  ~ Copyright (C) 2005 - 2011 Jaspersoft Corporation. All rights reserved.
  ~ http://www.jaspersoft.com.
  ~ Licensed under commercial Jaspersoft Subscription License Agreement
  --%>

<tbody id="tableDetails" class="copyTo">
{{ var totalsMessage = '<spring:message code="ADH_088_TABLE_TOTALS" javaScriptEscape="true"/>';}}
{{ _.each(table.flattenedData, function(member, index) { }}

    {{ if (member.isGroupMember) { }}
        <!-- START: Group -->
        {{ if (member.isFooter && table.showTableTotals) { }}
            <tr id="{{=member.id}}"
                class="{{=member.rowClass}} memberSummaries"
                data-index="{{=index}}"
                data-value="{{=member.formattedValue}}"
                data-mask="{{=member.group.mask}}"
                data-type="{{=member.group.numericType}}"
                data-fieldDisplay="{{=member.group.defaultDisplayName}}"
                data-label="{{=member.group.currentDisplayName}}"
                data-fieldName="{{=member.group.name}}">
                {{ _.each(member.groupSummaryRow.members, function(footerCell, index) { }}
                    {{ if (index === 0) { }}
                        <td class="value">{{=formatString(totalsMessage, member.formattedValue)}}</td>
                    {{ } else { }}
                        <td class="value {{ footerCell.isNumeric ? print('numeric') : print('') }}">{{=footerCell.formattedContent}}</td>
                    {{ } }}
                {{ }); }}
            </tr>
        {{ } else { }}
            <tr class="placeholder member labels">
                <td class="label" colspan="{{ print(table.columns.length); }}">
                    <span id="{{=member.id}}" class="{{=member.rowClass}} placeholder member labels" >{{=member.formattedValue}}</span>
                </td>
            </tr>
        {{ } }}
<!-- END: Group -->
    {{ } }} 
    {{ if (member.isRow && table.showTableDetails) { }}
        {{ if (table.hasColumns) { }}
            <tr class="record">
                {{ _.each(member.members, function(detailCell, index) { }}
                    <td class="value {{ detailCell.isNumeric ? print('numeric') : print(''); }}">
                        <span class="wrap">{{!detailCell.formattedContent ? print('&nbsp;') : print(detailCell.formattedContent) }}</span>
                    </td>
                {{ }); }}
            </tr>
        {{ } }}
    {{ } }}
{{ }); }}
<!-- END: Group and Rows-->

<!-- START: Summary Row -->
{{ if (table.hasSummaryRow) { }}
    <tr id="grandSummaryRow" class="grand columnSummaries stripe">
        {{ _.each(table.summaryRow.members, function(summaryCell, index) { }}
            {{ if (summaryCell.isEmpty) { }}
            <td id="grandSummaryCell_{{=index}}"
                data-summaryIndex="{{=index}}"
                data-fieldName="{{=table.columns[index].name}}"
                class="value"></td>
            {{ } else { }}
            <td id="grandSummaryCell_{{=index}}"
                class="value {{ summaryCell.isNumeric ? print('numeric') : print(''); }}"
                data-summaryIndex="{{=index}}"
                data-fieldName="{{=table.columns[index].name}}"
                data-name="{{=table.columns[index].summaryFunction}}">{{=summaryCell.formattedContent}}
            </td>
            {{ } }}
        {{ }); }}
    </tr>
{{ } }} 
<!-- END: Summary Row -->
<!-- START: EOF file test -->
{{ if (endOfFile) { }}
    <tr id="endOfFileRow"></tr>
{{ } }}
<!-- END: EOF file test -->
</tbody>

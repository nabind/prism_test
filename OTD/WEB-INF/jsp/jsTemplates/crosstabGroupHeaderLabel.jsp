<%--
~ Copyright (C) 2005 - 2011 Jaspersoft Corporation. All rights reserved.
~ http://www.jaspersoft.com.
~ Licensed under commercial Jaspersoft Subscription License Agreement
--%>
{{ var sortIcon = _sortStatus == 1 ? "ascending" : (_sortStatus == 2 ? "descending" : "natural"); }}
<th
    class="{{=_headerClass}}"
    id="{{=_id}}"
    data-level="{{-_levelName}}"
    data-dimension="{{-_dimensionName}}"
    data-expanable="{{=_isExpandable}}"
    data-sorting="{{=sortIcon}}"
    {{ if (_rowspan !== null) { }}
        rowspan="{{=_rowspan}}"
    {{ } }}
    {{ if (_colspan !== null) { }}
        colspan="{{=_colspan}}"
    {{ } }}>
    {{ if (_isExpandable) { }}
        <span class="button disclosure icon {{_isLevelExpanded ? print('open') : print('closed');}}"></span>
    {{ } }}

    {{-_cellContent}}

    {{ if (_canSort) { }}
    <span class="icon button sort {{=sortIcon}}"></span>
    {{ } }}
</th>

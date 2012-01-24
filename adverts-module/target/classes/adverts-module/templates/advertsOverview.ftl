

[#-------------- INCLUDE AND ASSIGN PART --------------]

[#-- Include: Global --]
[#include "../teasers/init.inc.ftl"]
[#include "../macros/pagination.ftl"]

[#-- Assigns: General --]
[#assign cms=JspTaglibs["cms-taglib"]]

[#-- Assigns: Get and check Latest Adverts --]
[#assign pager = model.pager]
[#assign advertsList = pager.pageItems!]
[#assign hasAdvertsList = advertsList?has_content]

[#-- Assigns: For subtitle containing ${timePeriod} entered by the used in the dialog --]
[#assign timePeriod]
    [#if ctx.date??]
        ${model.minDate?date?string.long}
    [#else]
        ${model.minDate?string('MMMM')}
    [/#if]
[/#assign]
[#assign subtitle = ('"'+(content.subtitle!)+'"')?eval]

[#-- Assigns: Macro for Item iteration --]
[#macro assignItemValues item]
    [#-- Assigns: Get Content from List Item--]
    [#assign itemTitle = item.title!item.@name]
    [#assign itemText = stk.abbreviateString(item.abstract!, 100)]
    [#assign itemLink = mgnl.createLink(item)!]
    [#assign itemDate = item.date!]
    [#assign itemLocation = item.location!]

    [#-- Assigns: Is Content Available of Item--]
    [#assign hasText = itemText?has_content]
    [#assign hasDate = itemDate?has_content]
    [#assign hasLocation = itemLocation?has_content]
    [#assign showText = hasText && content.showAbstract]
[/#macro]

[#-------------- RENDERING PART --------------]

[#-- Rendering: Latest Adverts --]
<div class="${divClass}" ${divID}>
    [@cms.editBar editLabel="${i18n['advertsOverview.editLabel.paragraph']}" moveLabel="" deleteLabel="" /]

    <${headingLevel}>${subtitle}</${headingLevel}>

    <div id="advert-pager">
        <h5>${i18n['advertsOverview.advertPager.title']}</h5>

        <ul>
            <li class="prev"><a href="${model.prevLink.href}" title="${i18n['advertsOverview.advertPager.prevMonth']}">${model.prevLink.title}</a></li>
            <li class="next"><a href="${model.nextLink.href}" title="${i18n['advertsOverview.advertPager.nextMonth']}">${model.nextLink.title}</a></li>
        </ul>
    </div><!-- #advert-pager -->

    [#-- Macro: Pager --]
    [@pagination pager "top" /]

    [#if hasAdvertsList]
        <ul>
        [#list advertsList as item]
            [#-- Macro: Item Assigns --]
            [@assignItemValues item=item/]

            [#-- Rendering: List Item rendering --]
            <li>
                [#if hasDate]
                    <div class="date">
                        <span class="month"><abbr title="${itemDate?string("MMMM")}">${itemDate?string("MMM")}</abbr></span>
                        <span class="day">${itemDate?string("d")}<span>.</span></span>
                    </div>
                [/#if]

                <h3>
                    <a href="${itemLink}">${itemTitle}</a>
                </h3>

                [#if hasLocation || hasDate || showText]
                <ul class="advert-data">
                    [#if hasLocation]
                        <li class="location">${itemLocation}</li>
                    [/#if]
                    [#if hasDate]
                        <li class="time">${itemDate?time?string.short}</li>
                    [/#if]
                    [#if showText]
                        <li class="organizer">${itemText}</li>
                    [/#if]
                </ul>
            [/#if]
            </li>
        [/#list]
        </ul>
    [#else]
        <ul>
            <p>${i18n['advertsList.noResults']}</p>
        </ul>
    [/#if]
</div><!-- end ${divClass} -->
[#-- Macro: Pager --]
[@pagination pager "bottom"/]

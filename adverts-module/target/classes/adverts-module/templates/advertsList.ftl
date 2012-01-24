

[#-------------- INCLUDE AND ASSIGN PART --------------]

[#-- Include: Global --]
[#include "init.inc.ftl"]

[#-- Assigns: Get and check Latest Adverts --]
[#assign latestAdverts = model.items!]
[#assign hasLatestAdverts = latestAdverts?has_content]

[#-- Assigns: Macro for Item iteration --]
[#macro assignItemValues item]
    [#-- Assigns: Get Content from List Item--]
    [#assign itemTitle = item.title!item.@name]
    [#assign itemText = stk.abbreviateString(item.abstract!, 100)]
    [#assign itemLink = mgnl.createLink(item)!]
    [#assign itemDate = item.date]
    [#assign itemLocation = item.location]

    [#-- Assigns: Is Content Available of Item--]
    [#assign hasText = itemText?has_content]
    [#assign hasDate = itemDate?has_content]
    [#assign hasLocation = itemLocation?has_content]
    [#assign showText = hasText && content.showAbstract]
[/#macro]



[#-------------- RENDERING PART --------------]

[#-- Rendering: Latest Adverts --]
<div class="${divClass}" ${divID}>
    [@cms.editBar /]
    <${headingLevel}>${content.teaserTitle}</${headingLevel}>

    [#if hasLatestAdverts]
        <ul>
        [#list latestAdverts as item]
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
        [#if model.allAdvertsLink??]
            <p class="all"><a href="${model.allAdvertsLink}">${i18n['advertsList.link.allAdverts']}</a></p>
        [/#if]
    [#else]
        <p>${i18n['advertsList.noResults']}</p>
    [/#if]
</div><!-- end ${divClass} -->


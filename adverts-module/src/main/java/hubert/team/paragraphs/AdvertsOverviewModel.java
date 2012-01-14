/**
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the Common Public Attribution License.
 * You may elect to use one or the other of these licenses as
 * indicated below.
 *
 * The contents of this file are subject to the Common Public
 * Attribution License Version 1.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.magnolia-cms.com/cpal.
 * The License is based on the Mozilla Public License Version 1.1
 * but Sections 14 and 15 have been added to cover use of software
 * over a computer network and provide for limited attribution for
 * the Original Developer. In addition, Exhibit A has been modified
 * to be consistent with Exhibit B.
 *
 * Software distributed under the License is distributed on an
 * AS-IS basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is the Magnolia Templating Kit.
 * The Original Developer is the Initial Developer.
 * The Initial Developer of the Original Code is Magnolia International Ltd.
 * All portions of the code written by Initial Developer are
 * Copyright (c) 2008-2011 the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 *
 *
 *
 * Alternatively, the contents of this file may be used under the
 * terms of the Magnolia Network Agreement license (the MNA), in
 * which case the provisions of the MNA are applicable instead of
 * those above. As a licensee of Magnolia, you have received a signed
 * copy of the terms and conditions of the MNA.
 *
 * If you wish to allow use of your version of this file only under
 * the terms of the MNA and not to allow others to use your version
 * of this file under the CPAL, indicate your decision by deleting
 * the provisions above and replace them with the notice and other
 * provisions required by the MNA. If you do not delete the provisions
 * above, a recipient may use your version of this file under either
 * the CPAL or the MNA (if he is a Magnolia Enterprise Edition licensee).
 *
 */
package hubert.team.paragraphs;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.i18n.I18nContentSupportFactory;
import info.magnolia.context.MgnlContext;
import info.magnolia.module.templating.MagnoliaTemplatingUtilities;
import info.magnolia.module.templating.RenderableDefinition;
import info.magnolia.module.templating.RenderingModel;
import info.magnolia.module.templatingkit.navigation.Link;
import info.magnolia.module.templatingkit.paragraphs.AbstractDateContentModel;
import info.magnolia.module.templatingkit.util.STKDateContentUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author hubert.team
 *
 */

public class AdvertsOverviewModel extends AbstractDateContentModel {


    public AdvertsOverviewModel(Content content, RenderableDefinition definition, RenderingModel parent) {
        super(content, definition, parent);
    }

    protected void sort(List<Content> itemList) {
        STKDateContentUtil.sortDateContentList(itemList, STKDateContentUtil.DESCENDING);
    }

    protected int getMaxResults() {
        return Integer.MAX_VALUE;
    }

    public Calendar getMinDate() {
        final String dateParameter = MgnlContext.getParameter("date");
        if(StringUtils.isNotEmpty(dateParameter)){
            Date date;
            try {
                date = new SimpleDateFormat("yyyy.MM.dd").parse(dateParameter);
            }
            catch (ParseException e) {
                throw new IllegalArgumentException("Can't parse date " + dateParameter);
            }
            minDate.setTimeInMillis(date.getTime());
        }
        else{
            String selector = MgnlContext.getAggregationState().getSelector();
            if(StringUtils.isNotEmpty(selector)){
                manipulateBySelector(minDate, selector);
            }

            this.minDate.set(Calendar.DATE, minDate.getActualMinimum(Calendar.DATE));
            this.minDate.set(Calendar.HOUR_OF_DAY, minDate.getActualMinimum(Calendar.HOUR_OF_DAY));
            this.minDate.set(Calendar.MINUTE, minDate.getActualMinimum(Calendar.MINUTE));
            // If not set the events will be shifted to the previous day on the calendar paragraph if no time has been given eg. dd.MM.yyyy 00.00.0000.
            this.minDate.set(Calendar.SECOND, minDate.getActualMinimum(Calendar.SECOND));
            this.minDate.set(Calendar.MILLISECOND, minDate.getActualMinimum(Calendar.MILLISECOND));
        }
        return minDate;
    }

    public Calendar getMaxDate() {
        final String dateParameter = MgnlContext.getParameter("date");
        if(StringUtils.isNotEmpty(dateParameter)){
            Date date;
            try {
                date = new SimpleDateFormat("yyyy.MM.dd").parse(dateParameter);
            }
            catch (ParseException e) {
                throw new IllegalArgumentException("Can't parse date " + dateParameter);
            }
            maxDate.setTimeInMillis(date.getTime());
        }
        else{
            String selector = MgnlContext.getAggregationState().getSelector();
            if(StringUtils.isNotEmpty(selector)){
                manipulateBySelector(maxDate, selector);
            }
            this.maxDate.set(Calendar.DATE, maxDate.getActualMaximum(Calendar.DATE));
        }
        this.maxDate.set(Calendar.HOUR_OF_DAY, minDate.getActualMaximum(Calendar.HOUR_OF_DAY));
        this.maxDate.set(Calendar.MINUTE, minDate.getActualMaximum(Calendar.MINUTE));
        this.maxDate.set(Calendar.SECOND, minDate.getActualMaximum(Calendar.SECOND));
        this.maxDate.set(Calendar.MILLISECOND, minDate.getActualMaximum(Calendar.MILLISECOND));

        return maxDate;
    }

    private void manipulateBySelector(Calendar monthToManipulate, String selector) {
            int year = Integer.parseInt(selector.substring(0, selector.indexOf(".")));
            int month = Integer.parseInt(selector.substring(selector.indexOf(".")+1));

            monthToManipulate.set(Calendar.YEAR, year);
            monthToManipulate.set(Calendar.MONTH, month-1); //MONTH starts with 00 -> minus one
    }

    public Link getPrevLink(){
        final Calendar prevMonth = getPrevMonth(minDate);
        return getLink(prevMonth);
    }

    public Link getNextLink(){
        final Calendar nextMonth = getNextMonth(minDate);
        return getLink(nextMonth);
    }

    private Link getLink(final Calendar monthToShow) {

        return new Link(){
            public String getHref() {
                String newURL = MagnoliaTemplatingUtilities.getInstance().createLink(MgnlContext.getAggregationState().getMainContent());
                newURL = StringUtils.removeEnd(newURL, ".html");
                newURL = StringUtils.removeEnd(newURL, "." + MgnlContext.getAggregationState().getSelector());
                newURL += DateFormatUtils.format(monthToShow, ".yyyy.MM") + ".html";
                return newURL;
            }
            public String getNavigationTitle() {
                return null;
            }
            public String getTitle() {
                return DateFormatUtils.format(monthToShow, "MMMM" , I18nContentSupportFactory.getI18nSupport().getLocale());
            }
        };
    }

    private Calendar getPrevMonth(Calendar ankerMonth){
        Calendar decremented = (Calendar) ankerMonth.clone();
        decremented.add(Calendar.MONTH, -1);
        return decremented;
    }

    private Calendar getNextMonth(Calendar ankerMonth){
        Calendar increment = (Calendar) ankerMonth.clone();
        increment.add(Calendar.MONTH, 1);
        return increment;
    }

    public String getMonth(){
        return DateFormatUtils.format(minDate, "MMMM");
    }



}
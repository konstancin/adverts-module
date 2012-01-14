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
import info.magnolia.cms.util.NodeDataUtil;
import info.magnolia.module.templating.MagnoliaTemplatingUtilities;
import info.magnolia.module.templating.RenderableDefinition;
import info.magnolia.module.templating.RenderingModel;
import info.magnolia.module.templatingkit.paragraphs.AbstractDateContentModel;
import info.magnolia.module.templatingkit.templates.category.TemplateCategory;
import info.magnolia.module.templatingkit.util.STKDateContentUtil;
import info.magnolia.module.templatingkit.util.STKUtil;

import javax.jcr.RepositoryException;
import java.util.Calendar;
import java.util.List;


/**
 * @author hubert.team
 *
 */

public class AdvertsListModel extends AbstractDateContentModel {


    public AdvertsListModel(Content content, RenderableDefinition definition, RenderingModel parent) {
        super(content, definition, parent);
    }

    protected void sort(List<Content> itemList) {
        STKDateContentUtil.sortDateContentList(itemList, STKDateContentUtil.DESCENDING);
    }

    protected int getMaxResults() {
        return (int) NodeDataUtil.getLong(content, "maxResults", Integer.MAX_VALUE);
    }

    public Calendar getMinDate() {
        return minDate;
    }

    public Calendar getMaxDate() {
        this.maxDate.set(Calendar.YEAR, maxDate.getActualMaximum(Calendar.YEAR));
        return maxDate;
    }

    public String getAllEventsLink() throws RepositoryException{
        final Content allEventsPage = STKUtil.getNearestContentByTemplateCategorySubCategory(getSearchRoot(), TemplateCategory.FEATURE, "eventsOverview", content);
        return new MagnoliaTemplatingUtilities().createLink(allEventsPage);
    }

}

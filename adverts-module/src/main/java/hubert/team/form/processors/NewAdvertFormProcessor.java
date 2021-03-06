package hubert.team.form.processors;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.ItemType;
import info.magnolia.cms.security.AccessDeniedException;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.module.form.processors.AbstractFormProcessor;
import info.magnolia.module.form.processors.FormProcessorFailedException;

import javax.jcr.RepositoryException;
import java.util.Map;

public class NewAdvertFormProcessor extends AbstractFormProcessor {

    @Override
    protected void internalProcess(Content form, Map<String, Object> map) throws FormProcessorFailedException {
        Content advertsFolder = ContentUtil.getContent("website", "/konstancin/ogloszenia/");
        Content newAdvert = createNewContent(advertsFolder);
        copyContentData("title", map, newAdvert);
        copyContentData("abstract", map, newAdvert);
        copyContentData("location", map, newAdvert);
        copyContentData("contact", map, newAdvert);
        copyContentData("site",map,newAdvert);
        setTemplate(newAdvert);
        setAuthor(newAdvert);
        save(newAdvert);
    }

    private void save(Content content) {
        try {
            content.save();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private void setTemplate(Content newAdvert) {
        try {
            newAdvert.getMetaData().setTemplate("stkAdvert");
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }

    private void setAuthor(Content newAdvert) {
        try {
            newAdvert.getMetaData().setAuthorId(MgnlContext.getUser().getName());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }


    private void copyContentData(String key, Map<String, Object> map, Content newAdvert) {
        try {
            newAdvert.setNodeData(key, map.get(key));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private Content createNewContent(Content advertsFolder) {
        try {
            advertsFolder.save();
            Content newAdvert =  advertsFolder.createContent("ogloszenie", ItemType.CONTENT);
            advertsFolder.save();
            newAdvert.save();
            return newAdvert;
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}

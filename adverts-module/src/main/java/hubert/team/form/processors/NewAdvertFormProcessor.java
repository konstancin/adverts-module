package hubert.team.form.processors;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.ItemType;
import info.magnolia.cms.security.AccessDeniedException;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.module.form.processors.AbstractFormProcessor;
import info.magnolia.module.form.processors.FormProcessorFailedException;

import javax.jcr.RepositoryException;
import java.util.Map;

public class NewAdvertFormProcessor extends AbstractFormProcessor {

    @Override
    protected void internalProcess(Content form, Map<String, Object> map) throws FormProcessorFailedException {
        Content advertsFolder = ContentUtil.getContent("website", "/konstancin/ogloszenia/");
        Content newAdvert = createNewContent(advertsFolder);
        copyContentData("location", form, newAdvert);
        copyContentData("title", form, newAdvert);
        copyContentData("abstract", form, newAdvert);
        copyContentData("advertTitle", form, newAdvert);
        setTemplate(newAdvert);
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

    private void copyContentData(String key, Content form, Content newAdvert) {
        try {
            newAdvert.setNodeData(key, form.getNodeData(key));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private Content createNewContent(Content advertsFolder) {
        try {
            Content newAdvert =  advertsFolder.createContent("bartek", ItemType.FOLDER);
            newAdvert.save();
            return newAdvert;
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}

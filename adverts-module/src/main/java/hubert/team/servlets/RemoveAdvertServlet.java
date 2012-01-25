package hubert.team.servlets;




import javax.jcr.RepositoryException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import info.magnolia.cms.core.*;
import info.magnolia.cms.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: tomeq
 * Date: 25.01.12
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public class RemoveAdvertServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Content advertsFolder = ContentUtil.getContent("website", "/konstancin/ogloszenia/");
        final String uuidToDelete = req.getParameter("uuid");

        try{
            Content advertToDelete = getContent(advertsFolder,uuidToDelete);
            if(advertToDelete == null)
                throw new RepositoryException(uuidToDelete);
            advertToDelete.delete();
            advertsFolder.save();
        }catch(RepositoryException exc){
            throw new ServletException("Nie znaleziono ogłoszenia o uuid "+exc);
        }

        resp.sendRedirect("/magnoliaAuthor/konstancin/ogloszenia.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req,resp);
    }
    
    public Content getContent(Content root, final String uuid){
        Content.ContentFilter contentFilter = new Content.ContentFilter(){
            public boolean accept(Content content) {
                return content.getUUID().equals(uuid);
            }
        };
        Collection<Content> adverts = root.getChildren(contentFilter);
        Iterator iterator = adverts.iterator();
        if(iterator.hasNext())
            return adverts.iterator().next();
        else
            return null;
    }
    
    public void deleteContent(Content root, Content content){

    }

}

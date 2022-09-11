package org.originit.analyze.controller;

import org.originit.analyze.entity.Book;
import org.originit.analyze.entity.BookDetails;
import org.originit.analyze.entity.Category;
import org.originit.analyze.util.ResourceUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class BookController {


    @Value("${outside.resource.path}")
    private String path;

    @Value("${outside.resource.prefix}")
    private String resourcePrefix;

    /**
     * 查询目录，若有分类则查看分类下的目录
     * @param category 书的分类
     * @throws IOException
     */
    @RequestMapping("/")
    public String index(Model model, @RequestParam(required = false) String category) throws IOException {
        List<Category> categoryList = handleCategories();
        if (category == null && !categoryList.isEmpty()) {
            category = categoryList.get(0).getName();
        }
        List<Book> books = Collections.EMPTY_LIST;
        if (category != null){
            books = handleBooks(category);
        }
        model.addAttribute("category",category);
        model.addAttribute("categories",categoryList);
        model.addAttribute("books",books);
        return "index";
    }

    /**
     * 处理url的中文
     * @return 编码后的url
     */
    private String encodeArg(String arg) {
        return URLEncoder.encode(arg);
    }

    /**
     * 处理分类下的所有书籍
     * @param category 分类名称
     * @return 该分类下的书籍的名称分类及详情url
     * @throws IOException
     */
    private List<Book> handleBooks(String category) throws IOException {
        List<Book> books = Collections.EMPTY_LIST;
        Resource[] booksRes = ResourceUtil.loadResources(path + "/" + category + "/*");
        if (booksRes.length > 0) {
            books = new ArrayList<>(booksRes.length);
            for (Resource bookRes : booksRes) {
                if (!bookRes.isFile()||bookRes.getFile().isFile()) {
                    continue;
                }
                Book book = new Book();
                book.setBookName(bookRes.getFilename());
                book.setCategory(category);
                book.setUrl("/book?category=" + encodeArg(category) + "&bookName=" + encodeArg(book.getBookName()));
                books.add(book);
            }
        }
        return books;
    }

    /**
     * 处理配置的outside.resource.path下的分类列表
     * @return 分类的相关信息
     * @throws IOException
     */
    private List<Category> handleCategories() throws IOException {
        Resource[] categoriesRes = ResourceUtil.loadResources(path + "/*");
        List<Category> categoryList = Collections.EMPTY_LIST;
        if (categoriesRes.length > 0) {
            categoryList = new ArrayList<>(categoriesRes.length);
            for (Resource resource : categoriesRes) {
                // 分类应该是文件夹类型
                if (!resource.isFile()||resource.getFile().isFile()) {
                    continue;
                }
                final Category c = new Category();
                c.setName(resource.getFilename());
                c.setUrl("/?category=" + encodeArg(c.getName()));
                categoryList.add(c);
            }
        }
        return categoryList;
    }


    /**
     * 查看书的所有内容
     * @param category 书分类
     * @param bookName 书名或者所在文件夹
     * @param link 当前是哪一章
     */
    @RequestMapping("/book")
    public String index(Model model,@RequestParam String category,@RequestParam String bookName,@RequestParam(required = false) String link) throws IOException {
        Resource[] resources = ResourceUtil.loadResources(path + "/" + category + "/" + bookName + "/*.html");

        List<BookDetails> booksDetails = new ArrayList<>(resources.length);
        // 遍历文件内容
        for(Resource resource : resources) {
            final BookDetails pageInfo = new BookDetails();
            pageInfo.setName(resource.getFilename());
            pageInfo.setUrl(resourcePrefix + "/" + category + "/" + encodeArg(bookName) + "/" + encodeArg(resource.getFilename()));
            pageInfo.setForwardUrl("/?link="+encodeArg(resource.getFilename()));
            booksDetails.add(pageInfo);
        }
        if (link == null) {
            if (booksDetails.size() > 0) {
                link = booksDetails.get(0).getUrl();
            }
        } else {
            // 处理中文
            final String[] split = link.split("/");
            split[split.length - 1] = encodeArg(split[split.length - 1]);
            link = String.join("",split);
        }
        model.addAttribute("booksDetails",booksDetails);
        model.addAttribute("link",link);
        model.addAttribute("bookName",bookName);
        return "book-details";
    }
}

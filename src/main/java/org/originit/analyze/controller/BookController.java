package org.originit.analyze.controller;

import org.originit.analyze.consts.BookConst;
import org.originit.analyze.entity.Book;
import org.originit.analyze.entity.BookDetails;
import org.originit.analyze.entity.Category;
import org.originit.analyze.util.ResourceUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@Controller
public class BookController {


    ReadWriteLock fileLock = new ReentrantReadWriteLock();

    @Value("${outside.resource.path}")
    private String path;

    @Value("${outside.resource.prefix}")
    private String resourcePrefix;

    /**
     * 查询目录，若有分类则查看分类下的目录
     *
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
        if (category != null) {
            books = handleBooks(category);
        }
        model.addAttribute("category", category);
        model.addAttribute("categories", categoryList);
        model.addAttribute("books", books);
        return "index";
    }

    /**
     * 处理url的中文
     *
     * @return 编码后的url
     */
    private String encodeArg(String arg) {
        try {
            return URLEncoder.encode(arg,"utf8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 处理分类下的所有书籍
     *
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
                if (!bookRes.isFile() || bookRes.getFile().isFile()) {
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
     *
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
                if (!resource.isFile() || resource.getFile().isFile()) {
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
     *
     * @param category 书分类
     * @param bookName 书名或者所在文件夹
     * @param link     当前是哪一章
     */
    @RequestMapping("/book")
    public String book(Model model, @RequestParam String category, @RequestParam String bookName, @RequestParam(required = false) String link,@RequestParam (required = false) String type) throws IOException {
        String suffix;
        String secondSuffix = null;
        if (type == null || !BookConst.SUFFIX_ALL.contains(type)) {
            suffix = BookConst.SUFFIX_HTML;
            secondSuffix = BookConst.SUFFIX_PDF;
        } else {
            suffix = type;
        }
        String finalSuffix = suffix;
        Resource[] resources = ResourceUtil.loadResources(path + "/" + category + "/" + bookName + "/*." + suffix);
        if (resources.length == 0 && secondSuffix != null) {
            finalSuffix = secondSuffix;
            resources = ResourceUtil.loadResources(path + "/" + category + "/" + bookName + "/*." + secondSuffix);
        }
        List<BookDetails> booksDetails = new ArrayList<>(resources.length);
        // 遍历文件内容
        for (Resource resource : resources) {
            final BookDetails pageInfo = new BookDetails();
            pageInfo.setName(resource.getFilename());
            pageInfo.setUrl(resourcePrefix + "/" + category + "/" + encodeArg(bookName) + "/" + encodeArg(resource.getFilename()));
            pageInfo.setForwardUrl("/?link=" + encodeArg(resource.getFilename()));
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
            link = String.join("", split);
        }
        model.addAttribute("fileType",finalSuffix);
        model.addAttribute("fileTypes",BookConst.SUFFIX_ALL);
        model.addAttribute("booksDetails", booksDetails);
        model.addAttribute("link", link);
        model.addAttribute("bookName", bookName);
        return "book-details";
    }

    private void resolveResources(String pattern, Consumer<Resource> resourceConsumer) {
        resolveResources(pattern, resourceConsumer, null);
    }

    private void resolveResources(String pattern, Consumer<Resource> resourceConsumer, Predicate<Resource> resourcePredicate) {
        Assert.notNull(resourceConsumer, "请传入consumer进行处理");
        Resource[] categoriesRes = ResourceUtil.loadResources(pattern);
        for (Resource resource : categoriesRes) {
            if (resourcePredicate != null && !resourcePredicate.test(resource)) {
                continue;
            }
            resourceConsumer.accept(resource);
        }
    }

    Predicate<Resource> dirPredicate = resource -> {
        try {
            return resource.isFile() && resource.getFile().isDirectory();
        } catch (IOException e) {
            return false;
        }
    };

    Predicate<Resource> filePredicate = resource -> {
        try {
            return resource.isFile() && resource.getFile().isFile();
        } catch (IOException e) {
            return false;
        }
    };

    @RequestMapping("/formatFiles")
    @ResponseBody
    public String formatFiles(@RequestParam(required = false) String category, @RequestParam(required = false) String bookName, @RequestParam String originStr, @RequestParam(defaultValue = "") String replacement, @RequestParam(defaultValue = "true") boolean includeDir) {
        Function<Resource, File> replaceFunc = res -> {
            try {
                final File dest = new File(Objects.requireNonNull(res.getFile().getAbsolutePath()).replace(originStr, replacement));
                boolean success = res.getFile().renameTo(dest);
                if (success) {
                    return dest;
                } else {
                    return res.getFile();
                }
            } catch (IOException e) {
                return null;
            }
        };
        if (category == null) {
            // 修改所有的书
            resolveResources(path + "/*", resource -> {
                // 重命名
                File file = replaceFunc.apply(resource);
                if (file == null) {
                    return;
                }
                // 递归处理分类下的书籍
                replaceBooksName(file.getName(), replaceFunc);
            }, dirPredicate);
        } else {
            if (bookName == null) {
                // 修改具体某本书
                replaceDetailsName(category, bookName, replaceFunc);
            } else {
                // 修改指定分类的书
                // 递归处理分类下的书籍
                replaceBooksName(category, replaceFunc);
            }
        }

        return "success";
    }

    private void replaceBooksName(String category, Function<Resource, File> replaceFunc) {
        resolveResources(path + "/" + category + "/*", bookRes -> {
            final File bookFile = replaceFunc.apply(bookRes);
            if (bookFile == null) {
                return;
            }
            replaceDetailsName(category, bookFile.getName(), replaceFunc);
        }, dirPredicate);
    }

    private void replaceDetailsName(String category, String bookName, Function<Resource, File> replaceFunc) {
        resolveResources(path + "/" + category + "/" + bookName + "/*", replaceFunc::apply, filePredicate);
    }
}

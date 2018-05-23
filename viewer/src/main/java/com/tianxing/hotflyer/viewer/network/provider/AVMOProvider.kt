package com.tianxing.hotflyer.viewer.network.provider

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.util.ArrayList
import java.util.LinkedHashMap

import com.tianxing.hotflyer.viewer.adapter.item.Actress
import com.tianxing.hotflyer.viewer.adapter.item.Genre
import com.tianxing.hotflyer.viewer.adapter.item.Movie
import com.tianxing.hotflyer.viewer.adapter.item.MovieDetail
import com.tianxing.hotflyer.viewer.adapter.item.Screenshot

/**
 * Project: JAViewer
 */
object AVMOProvider {

    fun parseMovies(html: String): List<Movie> {
        val document = Jsoup.parse(html)

        val movies = ArrayList<Movie>()

        for (box in document.select("a[class*=movie-box]")) {
            val img = box.select("div.photo-frame > img").first()
            val span = box.select("div.photo-info > span").first()

            val hot = span.getElementsByTag("i").size > 0

            val date = span.select("date")

            movies.add(
                    Movie.create(
                            img.attr("title"), //标题
                            date[0].text(), //番号
                            date[1].text(), //日期
                            img.attr("src"), //图片地址
                            box.attr("href"), //链接
                            hot                 //是否热门
                    )
            )
        }

        return movies
    }

    fun parseActresses(html: String): List<Actress> {
        val document = Jsoup.parse(html)

        val actresses = ArrayList<Actress>()

        for (box in document.select("a[class*=avatar-box]")) {
            val img = box.select("div.photo-frame > img").first()
            val span = box.select("div.photo-info > span").first()

            actresses.add(
                    Actress.create(
                            span.text(), //名字
                            img.attr("src"), //图片地址
                            box.attr("href") //链接
                    ))
        }

        return actresses
    }

    fun parseMoviesDetail(html: String): MovieDetail {
        val document = Jsoup.parse(html)
        val movie = MovieDetail()

        //General Parsing
        run {
            movie.title = document.select("div.container > h3").first().text()
            movie.coverUrl = document.select("[class=bigImage]").first().attr("href")
        }

        //Parsing Screenshots
        run {
            for (box in document.select("[class*=sample-box]")) {
                movie.screenshots.add(
                        Screenshot.create(
                                box.getElementsByTag("img").first().attr("src"),
                                box.attr("href")
                        )
                )
            }
        }

        //Parsing Actresses
        run {
            for (box in document.select("[class*=avatar-box]")) {
                movie.actresses.add(
                        Actress.create(
                                box.text(),
                                box.getElementsByTag("img").first().attr("src"),
                                box.attr("href")
                        )
                )
            }
        }

        //Parsing Headers
        run {
            val info = document.select("div.info").first()
            if (info !=
                    null) {
                for (p in info.select("p:not([class*=header]):has(span:not([class=genre]))")) {
                    val strings = p.text().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    movie.headers.add(MovieDetail.Header.create(
                            strings[0].trim { it <= ' ' },
                            if (strings.size > 1) strings[1].trim { it <= ' ' } else "", ""
                    ))
                }

                run {
                    val headerNames = ArrayList<String>()
                    val headerAttr = ArrayList<Array<String>>()

                    for (p in info.select("p[class*=header]")) {
                        headerNames.add(p.text().replace(":", ""))
                    }

                    for (a in info.select("p > a")) {
                        headerAttr.add(arrayOf(a.text(), a.attr("href")))
                    }

                    for (i in 0 until Math.min(headerNames.size, headerAttr.size)) {
                        movie.headers.add(
                                MovieDetail.Header.create(
                                        headerNames[i],
                                        headerAttr[i][0].trim { it <= ' ' },
                                        headerAttr[i][1].trim { it <= ' ' }
                                )
                        )
                    }
                }

                for (a in info.select("* > [class=genre] > a")) {
                    movie.genres.add(
                            Genre.create(
                                    a.text(),
                                    a.attr("href")
                            )
                    )
                }
            }
            return movie
        }
    }

    fun parseGenres(html: String): LinkedHashMap<String, MutableList<Genre>> {
        val map = LinkedHashMap<String, MutableList<Genre>>()

        val container = Jsoup.parse(html).getElementsByClass("pt-10").first()
        val keys = ArrayList<String>()
        for (e in container.getElementsByTag("h4")) {
            keys.add(e.text())
        }

        val genres = ArrayList<MutableList<Genre>>()
        for (element in container.getElementsByClass("genre-box")) {
            val list = ArrayList<Genre>()
            for (e in element.getElementsByTag("a")) {
                list.add(Genre.create(e.text(), e.attr("href")))
            }
            genres.add(list)
        }

        for (i in keys.indices) {
            map.put(keys[i], genres[i])
        }

        return map
    }
}

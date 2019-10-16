package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wuyan.film.FilmService;
import com.wuyan.film.service.FilmModuleService;
import com.wuyan.film.vo.BaseRespVO;
import com.wuyan.film.vo.FilmDetailResponseVO;
import com.wuyan.film.vo.FilmQueryRequestVO;
import com.wuyan.film.vo.FilmQueryResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class FilmModuleController {

//    @Reference(interfaceClass = FilmModuleService.class,check = false)
//    FilmModuleService filmModuleService;
//
//    @RequestMapping("/film/films/{id}")
//    public BaseRespVO getFilmsDetail(@PathVariable(value = "id") Integer id){
//        FilmDetailResponseVO filmDetail = filmModuleService.getFilmDetail(id);
//        return BaseRespVO.ok(filmDetail);
//    }
//
//    @RequestMapping("film/getFilms")
//    public BaseRespVO getRelevantFilm(FilmQueryRequestVO filmQueryRequestVO){
//
//        if(filmQueryRequestVO.getShowType() != null){
//            BaseRespVO baseRespVO = filmModuleService.getFilmsByShowType(filmQueryRequestVO);
//            return baseRespVO;
//        }
//        if (filmQueryRequestVO.getKw() != null){
//            FilmQueryResult filmsByKeyword = filmModuleService.getFilmsByKeyword(filmQueryRequestVO.getKw());
//            return BaseRespVO.ok(filmsByKeyword);
//        }
//        return BaseRespVO.serviceErr();
//    }
}

package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.modular.vo.ConditionVO;
import com.stylefeng.guns.rest.modular.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.vo.ReBaseDataVo;
import com.stylefeng.guns.rest.modular.vo.ResultVO;
import com.wuyan.film.FilmService;
import com.wuyan.film.vo.FilmConditionVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("film")
public class FilmController {
    @Reference(interfaceClass = FilmService.class)
    private FilmService filmService;

    @RequestMapping("getIndex")
    public ResultVO filmIndex(){
        FilmIndexVO filmIndex = new FilmIndexVO();
        filmIndex.setBanners(filmService.getBanner());
        filmIndex.setHotFilms(filmService.getHotFilm(0,false));
        filmIndex.setSoonFilms(filmService.getSoonFilm(0,false));
        filmIndex.setBoxRanking(filmService.getBoxRanking(0));
        filmIndex.setExpectRanking(filmService.getExpectRanking(0));
        filmIndex.setTop100(filmService.getTop100(0));
        return ResultVO.ok(filmIndex);
    }

    @RequestMapping("getConditionList")
    public ReBaseDataVo getConditionList(FilmConditionVO filmConditionVO){
        ConditionVO conditionVO = new ConditionVO();
        conditionVO.setCatInfo(filmService.getCat(filmConditionVO.getCatId()));
        conditionVO.setSourceInfo(filmService.getSource(filmConditionVO.getSourceId()));
        conditionVO.setYearInfo(filmService.getYear(filmConditionVO.getYearId()));
        return ReBaseDataVo.ok(conditionVO);
    }
}

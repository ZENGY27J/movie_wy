package com.stylefeng.guns.rest.modular.vo;

import com.wuyan.film.vo.CatVO;
import com.wuyan.film.vo.SourceVO;
import com.wuyan.film.vo.YearVO;

import java.util.List;

public class ConditionVO {
    List<CatVO> catInfo;
    List<SourceVO> sourceInfo;
    List<YearVO> yearInfo;

    public List<CatVO> getCatInfo() {
        return catInfo;
    }

    public void setCatInfo(List<CatVO> catInfo) {
        this.catInfo = catInfo;
    }

    public List<SourceVO> getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(List<SourceVO> sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    public List<YearVO> getYearInfo() {
        return yearInfo;
    }

    public void setYearInfo(List<YearVO> yearInfo) {
        this.yearInfo = yearInfo;
    }
}

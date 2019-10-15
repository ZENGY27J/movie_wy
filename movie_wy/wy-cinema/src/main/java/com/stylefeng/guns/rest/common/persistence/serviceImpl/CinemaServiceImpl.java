package com.stylefeng.guns.rest.common.persistence.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeAreaDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeBrandDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeHallDictTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeAreaDictT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeBrandDictT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeCinemaT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeHallDictT;
import com.wuyan.cinemaService.CinemaService;
import com.wuyan.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-10-14 15:27
 * @Description
 */
@Component
@Service(interfaceClass = CinemaService.class)
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    MtimeCinemaTMapper mtimeCinemaTMapper;

    @Autowired
    MtimeAreaDictTMapper mtimeAreaDictTMapper;

    @Autowired
    MtimeBrandDictTMapper mtimeBrandDictTMapper;

    @Autowired
    MtimeHallDictTMapper mtimeHallDictTMapper;

    /**
     * 根据查询条件获取影院列表
     * */
    @Override
    public CinemaListVo cinemaList(CinemaQueryVo cinemaQueryVo) {
        CinemaListVo cinemaListVo = new CinemaListVo();

        Page<MtimeCinemaT> page=new Page<>();

        page.setSize(cinemaQueryVo.getPageSize());
        page.setCurrent(cinemaQueryVo.getNowPage());
        page.setSearchCount(true);

        EntityWrapper<MtimeCinemaT> entityWrapper = new EntityWrapper<>();
        if (!cinemaQueryVo.getBrandId().equals(99)){
            entityWrapper.eq("brand_id",cinemaQueryVo.getBrandId());
        }
        if (!cinemaQueryVo.getDistrictId().equals(99)){
            entityWrapper.eq("area_id",cinemaQueryVo.getDistrictId());
        }
        if (!cinemaQueryVo.getHallType().equals(99)){
            entityWrapper.like("hall_ids","#"+cinemaQueryVo.getHallType()+"#");
        }
        List<MtimeCinemaT> cinemaList= mtimeCinemaTMapper.selectPage(page, entityWrapper);
        List<CinemaVo> cinemaVos = mtimeCinemaT2cinemavo(cinemaList);

        cinemaListVo.setData(cinemaVos);
        cinemaListVo.setNowPage(cinemaQueryVo.getNowPage());
        cinemaListVo.setStatus(0);
        int total=0;
        if (page.getTotal()%cinemaQueryVo.getPageSize()==0){
            total=(int) (page.getTotal()/cinemaQueryVo.getPageSize());
        }else {
            total=(int) (page.getTotal()/cinemaQueryVo.getPageSize())+1;
        }
        cinemaListVo.setTotalPage(total);
        return cinemaListVo;

        /*List<MtimeCinemaT> cinemaList= mtimeCinemaTMapper.selectPage(page, entityWrapper);
        if (!cinemaQueryVo.getHallType().equals(99)) {
            List<MtimeCinemaT> cinemaList2 = new ArrayList<>();
            for (MtimeCinemaT mtimeCinemaT : cinemaList) {
                String[] split = mtimeCinemaT.getHallIds().split("#");
                List<String> strings = Arrays.asList(split);
                if(strings.contains(String.valueOf(cinemaQueryVo.getHallType()))){
                    cinemaList2.add(mtimeCinemaT);
                }
            }
            List<CinemaVo> cinemaVos = mtimeCinemaT2cinemavo(cinemaList2);

            cinemaListVo.setData(cinemaVos);
            cinemaListVo.setNowPage(cinemaQueryVo.getNowPage());
            cinemaListVo.setStatus(0);
            int total=0;
            if (page.getTotal()%cinemaQueryVo.getPageSize()==0){
                total=(int) (page.getTotal()/cinemaQueryVo.getPageSize());
            }else {
                total=(int) (page.getTotal()/cinemaQueryVo.getPageSize())+1;
            }
            cinemaListVo.setTotalPage(total);
            return cinemaListVo;
        }
        List<CinemaVo> cinemaVos = mtimeCinemaT2cinemavo(cinemaList);

        cinemaListVo.setData(cinemaVos);
        cinemaListVo.setNowPage(cinemaQueryVo.getNowPage());
        cinemaListVo.setStatus(0);
        int total=0;
        if (page.getTotal()%cinemaQueryVo.getPageSize()==0){
            total=(int) (page.getTotal()/cinemaQueryVo.getPageSize());
        }else {
            total=(int) (page.getTotal()/cinemaQueryVo.getPageSize())+1;
        }
        cinemaListVo.setTotalPage(total);

        return cinemaListVo;
        */
    }


    /**
     * 获取影院查询条件
     * @param cinemaConditionQueryVo
     * @return
     */
    @Override
    public CinemaCoditionVo cinemaCodition(CinemaConditionQueryVo cinemaConditionQueryVo) {
        CinemaCoditionVo cinemaCoditionVo = new CinemaCoditionVo();
        CinemaCondition cinemaCondition = new CinemaCondition();

        //查询地区表
        EntityWrapper<MtimeAreaDictT> entityWrapper = new EntityWrapper<>();
        List<MtimeAreaDictT> mtimeAreaDictTS = mtimeAreaDictTMapper.selectList(entityWrapper);

        //将地区表转换成前端接收的vo
        List<AreaVo> areaVos = mtimeAreaDictsT2AreaVos(mtimeAreaDictTS,cinemaConditionQueryVo.getAreaId());
        cinemaCondition.setAreaList(areaVos);

        //查询品牌表
        EntityWrapper<MtimeBrandDictT> brandDictWrapper=new EntityWrapper<>();
        List<MtimeBrandDictT> brandDictTS=mtimeBrandDictTMapper.selectList(brandDictWrapper);

        //将查询的品牌列表转换成前端接收的Vo
        List<BrandVo> brandVos = mtimebrand2BrandVo(brandDictTS, cinemaConditionQueryVo.getBrandId());
        cinemaCondition.setBrandList(brandVos);

        //查询影厅类型--
        EntityWrapper<MtimeHallDictT> hallDictTEntityWrapper=new EntityWrapper<>();
        List<MtimeHallDictT> mtimeHallDictTS = mtimeHallDictTMapper.selectList(hallDictTEntityWrapper);

        //将查询的影厅类型转化成返回类型的vo
        List<HallTypeVo> hallTypeVos = mtimeHallDictT2halltypevo(mtimeHallDictTS, cinemaConditionQueryVo.getHallType());
        cinemaCondition.setHallTypeList(hallTypeVos);

        cinemaCoditionVo.setData(cinemaCondition);
        cinemaCoditionVo.setStatus(0);

        return cinemaCoditionVo;
    }

    private List<HallTypeVo> mtimeHallDictT2halltypevo(List<MtimeHallDictT> mtimeHallDictTS,Integer hallTypeId) {
        ArrayList<HallTypeVo> hallTypeVos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtimeHallDictTS)){
            for (MtimeHallDictT mtimeHallDictT:mtimeHallDictTS) {
                HallTypeVo hallTypeVo = new HallTypeVo();
                if (hallTypeId.equals(mtimeHallDictT.getUuid())){
                    hallTypeVo.setActive(true);
                }
                hallTypeVo.setHalltypeId(mtimeHallDictT.getUuid());
                hallTypeVo.setHalltypeName(mtimeHallDictT.getShowName());
                hallTypeVos.add(hallTypeVo);
            }
        }
        return hallTypeVos;
    }

    private List<BrandVo> mtimebrand2BrandVo(List<MtimeBrandDictT> brandDictTS,Integer brandId) {
        ArrayList<BrandVo> brandVos=new ArrayList<>();
        if (CollectionUtils.isNotEmpty(brandDictTS)){
            for (MtimeBrandDictT mtimeBrandDictT :brandDictTS) {
                BrandVo brandVo = new BrandVo();
                if (brandId.equals(mtimeBrandDictT.getUuid())){
                    brandVo.setActive(true);
                }
                brandVo.setBrandId(mtimeBrandDictT.getUuid());
                brandVo.setBrandName(mtimeBrandDictT.getShowName());
                brandVos.add(brandVo);
            }
        }
        return brandVos;
    }

    private List<AreaVo> mtimeAreaDictsT2AreaVos(List<MtimeAreaDictT> mtimeAreaDictTS,Integer areaId) {
        ArrayList<AreaVo> areaVos=new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtimeAreaDictTS)){
            for (MtimeAreaDictT mtimeAreaDictT:mtimeAreaDictTS) {
                AreaVo areaVo = new AreaVo();
                if (areaId.equals(mtimeAreaDictT.getUuid())){
                    areaVo.setActive(true);
                }
                areaVo.setAreaId(mtimeAreaDictT.getUuid());
                areaVo.setAreaName(mtimeAreaDictT.getShowName());
                areaVos.add(areaVo);
            }
        }
        return areaVos;
    }

    private List<CinemaVo> mtimeCinemaT2cinemavo(List<MtimeCinemaT> cinemaList) {
        ArrayList<CinemaVo> cinemaVoList=new ArrayList<>();
        if (CollectionUtils.isNotEmpty(cinemaList)){
            for (MtimeCinemaT cinemaT:cinemaList){
                CinemaVo cinemaVo=new CinemaVo();
                cinemaVo.setUuid(cinemaT.getUuid());
                cinemaVo.setCinemaName(cinemaT.getCinemaName());
                cinemaVo.setCinemaAddress(cinemaT.getCinemaAddress());
                cinemaVo.setMinimumPrice(cinemaT.getMinimumPrice());
                cinemaVoList.add(cinemaVo);
            }
        }
        return cinemaVoList;
    }
}





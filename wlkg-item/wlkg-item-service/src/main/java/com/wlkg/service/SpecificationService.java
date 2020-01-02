package com.wlkg.service;

import com.wlkg.mapper.SpecGroupMapper;
import com.wlkg.mapper.SpecParamMapper;
import com.wlkg.pojo.SpecGroup;
import com.wlkg.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;
    public List<SpecGroup> querySpecGroups(Long cid) {

        SpecGroup t = new SpecGroup();
        t.setCid(cid);
        return specGroupMapper.select(t);


    }

    public void add(SpecGroup specGroup) {
        specGroupMapper.insert(specGroup);

    }

    public void update(SpecGroup specGroup) {

        specGroupMapper.updateByPrimaryKeySelective(specGroup);
    }

    public void delete(Long id) {

        specGroupMapper.deleteByPrimaryKey(id);
        SpecParam specParam=new SpecParam();
        specParam.setGroupId(id);
        specParamMapper.delete(specParam);

    }


    //参数查询
    public List<SpecParam> querySpecParams(Long gid,Long cid,Boolean searching,Boolean generic) {
        SpecParam t = new SpecParam();
        t.setGroupId(gid);
        t.setCid(cid);
        t.setSearching(searching);
        t.setGeneric(generic);
        List<SpecParam> list = specParamMapper.select(t);

        return  list;
    }


    //添加参数
    public void addParam(SpecParam specParam) {
        specParamMapper.insert(specParam);

    }


    //修改参数
    public void updateParam(SpecParam specParam) {
        specParamMapper.updateByPrimaryKey(specParam);


    }

    //删除参数
    public void deleteParam(Long id) {
        specParamMapper.deleteByPrimaryKey(id);
    }



    public List<SpecGroup> querySpecsByCid(Long cid) {

        // 查询规格组
        List<SpecGroup> groups = this.querySpecGroups(cid);
        // 查询当前分类下的参数
        List<SpecParam> specParams = querySpecParams(null, cid, null, null);
        Map<Long, List<SpecParam>> map = new HashMap<>();

        for (SpecParam param : specParams){
            if(!map.containsKey(param.getGroupId())){
                //这个组id在map中不存在，新增一个list
                map.put(param.getGroupId(), new ArrayList<>());
            }

            map.get(param.getGroupId()).add(param);
        }
        //填充param到group
        for (SpecGroup specGroup: groups){
            specGroup.setParams(map.get(specGroup.getId()));
        }
        return groups;


    }
}

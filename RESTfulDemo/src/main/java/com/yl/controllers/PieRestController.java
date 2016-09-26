package com.yl.controllers;

import com.yl.dao.PieRepository;
import com.yl.entity.Pie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/pies")
public class PieRestController {

    @Autowired
    private PieRepository repository;

    /**
     * <p>
     * 查询所有数据
     * 可做分页
     * </p>
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<Pie>> getAllPies() {
        return new ResponseEntity<>((Collection<Pie>) repository.findAll(), HttpStatus.OK);
    }

    /**
     * <p>
     * 通过id查询
     * </p>
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Pie> getPieWithId(@PathVariable Long id) {
        return new ResponseEntity<>(repository.findOne(id), HttpStatus.OK);
    }

    /**
     * <p>
     * 通过其他参数查询
     * </p>
     *
     * @param name
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = {"name"})
    public ResponseEntity<List<Pie>> findPieWithName(@RequestParam(value = "name") String name) {
        return new ResponseEntity<>(repository.findByName(name), HttpStatus.OK);
    }


    /**
     * <p>
     * <p/>
     * 创建新的数据
     * 默认:
     * Content-Type = application/json;charset=UTF-8
     * </p>
     *
     * @param input
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addPie(@RequestBody Pie input) {
        return new ResponseEntity<>(repository.save(input), HttpStatus.CREATED);
    }


    /**
     * <p>
     *     上传图片;多张
     *     上传文件
     *
     * </p>
     * 上传图片:
     *
     * @param name
     * @param age
     * @param images
     * @param file1
     * @return
     */

    @RequestMapping(value = "/addFile", method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String addFile(@RequestParam(value ="name",required = false) String name,
                          @RequestParam(value ="age",required = false) String age,
                          @RequestParam(value ="images",required = false) MultipartFile[] images,
                          @RequestParam(value ="file1",required = false) MultipartFile file1) {


        System.out.println(name +" -- "+ age);
        System.out.println("上传多张图片:"+images.length);
        if(images.length >0 ) {
            for(int i=0;i<images.length;i++) {

                String imageName = images[i].getOriginalFilename();
                long size = images[i].getSize();
                System.out.println("图片"+i+"+:" + imageName + " ,大小:" + size);
            }
        }

        String imageName2 = file1.getOriginalFilename();
        long size2  = file1.getSize();
        System.out.println("HTML:"+imageName2 + " 大小:"+size2);


        return "success";
    }


}
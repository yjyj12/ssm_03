package com.qf.controller;

import com.qf.entity.BaseDict;
import com.qf.entity.Customer;
import com.qf.entity.QueryVo;
import com.qf.service.BaseDictService;
import com.qf.service.CustomerService;
import com.qf.utils.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class CustomerController {
    @Resource(name="baseDictService")
    private BaseDictService baseDictService;

//    @RequestMapping("/customerList")
//    public String customerList(Model model) {
//        // 客户来源
//        List<BaseDict> fromType = baseDictService.queryBaseDictByDictTypeCode("002");
//        // 所属行业
//        List<BaseDict> industryType = baseDictService.queryBaseDictByDictTypeCode("001");
//        // 客户级别
//        List<BaseDict> levelType = baseDictService.queryBaseDictByDictTypeCode("006");
//        // 把前端页面需要显示的数据放到模型中
//        model.addAttribute("fromType", fromType);
//        model.addAttribute("industryType", industryType);
//        model.addAttribute("levelType", levelType);
//
//        return "/customer";
//    }
   
@Resource(name = "customerService")
private CustomerService customerService;

    // 客户来源
    @Value("${CUSTOMER_FROM_TYPE}")
    private String CUSTOMER_FROM_TYPE;
    // 客户行业
    @Value("${CUSTOMER_INDUSTRY_TYPE}")
    private String CUSTOMER_INDUSTRY_TYPE;
    // 客户级别
    @Value("${CUSTOMER_LEVEL_TYPE}")
    private String CUSTOMER_LEVEL_TYPE;

    @RequestMapping("/customerList")
    public String customerList(Model model, QueryVo queryVo) {
        try {
            // 解决get请求乱码问题
            if (StringUtils.isNotBlank(queryVo.getCustName())) {
                queryVo.setCustName(new String(queryVo.getCustName().getBytes("UTF-8"), "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 客户来源
        List<BaseDict> fromType = baseDictService.queryBaseDictByDictTypeCode(CUSTOMER_FROM_TYPE);
        // 所属行业
        List<BaseDict> industryType = baseDictService.queryBaseDictByDictTypeCode(CUSTOMER_INDUSTRY_TYPE);
        // 客户级别
        List<BaseDict> levelType = baseDictService.queryBaseDictByDictTypeCode(CUSTOMER_LEVEL_TYPE);
        // 把前端页面需要显示的数据放到模型中
        model.addAttribute("fromType", fromType);
        model.addAttribute("industryType", industryType);
        model.addAttribute("levelType", levelType);

        // 分页查询数据
        Page<Customer> page = customerService.queryCustomerByQueryVo(queryVo);
        // 把分页查询的结果放到模型中
        model.addAttribute("page", page);

        // 数据回显
        model.addAttribute("custName", queryVo.getCustName());
        model.addAttribute("custSource", queryVo.getCustSource());
        model.addAttribute("custIndustry", queryVo.getCustIndustry());
        model.addAttribute("custLevel", queryVo.getCustLevel());

        return "/customer";
    }


    /**
     * 根据id查询用户,返回json格式数据
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Customer queryCustomerById(Long id) {

        System.out.println("ajax测试");
        Customer customer = customerService.queryCustomerById(id);
        System.out.println(customer + "编辑回显-----");
        return customer;
    }

    /**
     * 根据id查询用户,返回更新后客户的json格式数据
     */
    @RequestMapping("/update")
    @ResponseBody
    public String updateCustomerById(Customer customer) {
        customerService.updateCustomerById(customer);
        System.out.println("更新的方法");

        return "OK";
    }

    /**
     * 删除用户
     */
    @RequestMapping("delete")
    @ResponseBody
    public String deleteCustomerById(Long id) {
        customerService.deleteCustomerById(id);

        System.out.println("删除的方法");
        return "ok";
    }
}

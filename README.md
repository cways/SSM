# 1 SpringMVC

## 1 使用

```
@RequestMapping(value="/{seckillId}/detail",method=RequestMethod.GET)
public String detail(@PathVariable("seckillId")Long seckillId,Model model){
    if(seckillId == null){
        return "redirect:/seckill/list";
    }
    Seckill seckill = seckillService.getById(seckillId);
    if(seckill == null){
        return "forward:/seckill/list";
    }
    model.addAttribute("seckill",seckill);//model
    return "detail";//view
}
```

## 2 返回JSON数据

```
@RequestMapping(value="/{seckillId}/{md5}/execution",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public SeckillResult<Exposer> exportSeckillURL(@PathVariable("seckillId") long id){
    SeckillResult<Exposer> result;
    try{
        Exposer exposer = ...
    }catch(Exception e){
        logger.error(e.getMessage(),e);
    }
    return result;
}
```

## Cookie访问

```
@RequestMapping(value="/{seckillId}/{md5}/execution",method=RequestMethod.POST)
public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") long seckillId,
    @PathVariable("md5") String md5,@CookieValue(value="killPhone",required=false)Long phone){
        return null;
    }
```
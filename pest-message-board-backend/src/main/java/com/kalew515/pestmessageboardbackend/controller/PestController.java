package com.kalew515.pestmessageboardbackend.controller;

import com.kalew515.pestmessageboardbackend.checker.LoginChecker;
import com.kalew515.pestmessageboardbackend.interceptor.InterceptCheck;
import com.kalew515.pestmessageboardbackend.util.Response;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class PestController {
    @GetMapping("/pest")
    @InterceptCheck(checkers = {LoginChecker.class})
    public Response<HashMap<String, Object>> getPestData () {
        String url = "https://c.m.163.com/ug/api/wuhan/app/data/list-total?t=1581959283780";
        try {
            Document document = Jsoup.connect(url)
                                     .ignoreContentType(true)
                                     .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.106 Safari/537.36")
                                     .get();
            JSONObject jsonObject = new JSONObject(document.text());
            JSONObject totalData = jsonObject.getJSONObject("data")
                                             .getJSONObject("chinaTotal")
                                             .getJSONObject("total");
            JSONObject todayData = jsonObject.getJSONObject("data")
                                             .getJSONObject("chinaTotal")
                                             .getJSONObject("today");
            HashMap<String, JSONObject> dateTodayData = new HashMap<>();
            for (int i = 0; i < 10; i++) {
                JSONObject temp = jsonObject.getJSONObject("data")
                                            .getJSONArray("chinaDayList")
                                            .getJSONObject(jsonObject.getJSONObject("data")
                                                                     .getJSONArray("chinaDayList")
                                                                     .length() - i - 1);
                dateTodayData.put(temp.getString("date"), temp);
            }
            HashMap<String, HashMap<String, Object>> cityData = new HashMap<>();

            for (int i = 0; i < 34; i++) {
                HashMap<String, Object> temp = new HashMap<>();
                temp.put("name", jsonObject.getJSONObject("data")
                                           .getJSONArray("areaTree")
                                           .getJSONObject(2)
                                           .getJSONArray("children")
                                           .getJSONObject(i)
                                           .getString("name"));
                temp.put("id", jsonObject.getJSONObject("data")
                                         .getJSONArray("areaTree")
                                         .getJSONObject(2)
                                         .getJSONArray("children")
                                         .getJSONObject(i)
                                         .getString("id"));
                temp.put("totalData", jsonObject.getJSONObject("data")
                                                .getJSONArray("areaTree")
                                                .getJSONObject(2)
                                                .getJSONArray("children")
                                                .getJSONObject(i)
                                                .getJSONObject("total"));
                temp.put("todayData", jsonObject.getJSONObject("data")
                                                .getJSONArray("areaTree")
                                                .getJSONObject(2)
                                                .getJSONArray("children")
                                                .getJSONObject(i)
                                                .getJSONObject("today"));
                cityData.put(temp.get("name")
                                 .toString(), temp);
            }
            HashMap<String, Object> result = new HashMap<>();
            result.put("todayData", todayData);
            result.put("totalData", totalData);
            result.put("cityData", cityData);
            result.put("dateTodayData", dateTodayData);
            return Response.success("", result);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.invalid("Internal error");
        }
    }
}

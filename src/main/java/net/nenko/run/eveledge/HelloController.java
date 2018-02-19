package net.nenko.run.eveledge;

import lombok.Data;
import net.nenko.eveledge.Engine;
import net.nenko.eveledge.Event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    Engine engine;

    @RequestMapping("/")
    String hello() {
        return "Hello World!";
    }

    @Data
    static class Result {
        private final int left;
        private final int right;
        private final long answer;
    }

    @Data
    static class SaveResult {
        private final String result;
    }

    // SQL sample
    @RequestMapping("calc")
    Result calc(@RequestParam int left, @RequestParam int right) {
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("left", left)
                .addValue("right", right);
        return jdbcTemplate.queryForObject("SELECT :left + :right AS answer", source,
                (rs, rowNum) -> new Result(left, right, rs.getLong("answer")));
    }


    @RequestMapping("ws-save")
    SaveResult wsSave(@RequestParam String app,
    		    @RequestParam String obj,
    		    @RequestParam int event) {
    	Event ev = new Event(app, obj, event);

    	String engineResult = engine.save(ev);
        return new SaveResult(engineResult == null ? "OK" : engineResult); 
    }

    @RequestMapping("ws-save-prop2")
    SaveResult wsSaveProp2(@RequestParam String app,
    		    @RequestParam String obj,
    		    @RequestParam int event,
    		    @RequestParam String prop1,
    		    @RequestParam String prop2,
    		    @RequestParam String value1,
    		    @RequestParam String value2) {
    	Event ev = new Event(app, obj, event);
    	ev.addProp(prop1, value1);
    	ev.addProp(prop2, value2);

    	String engineResult = engine.save(ev);
        return new SaveResult(engineResult == null ? "OK" : engineResult); 
    }

    @RequestMapping("ws-find")
    SaveResult wsSave(@RequestParam String app,
    		    @RequestParam String obj) {
    	List<Event> ev = engine.find(app, obj);
        return new SaveResult(ev.toString()); 
    }


}

package gw.arg.service;

import io.confluent.avro.random.generator.Generator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
public class ArgController {
    private Dictionary<String, Generator> random_generator_store=new Hashtable<String, Generator>();

    @RequestMapping("/")
    public String index(){
        return "Welcome From Avro Random Generator";
    }
    @RequestMapping("/avro")
    public List<String> getAll(){
        List<String> list=new ArrayList<String>();
        for (Enumeration e = random_generator_store.keys(); e.hasMoreElements();) {
            list.add(e.nextElement().toString());
        }
        return list;
    }

    @RequestMapping(value = "/avro/{id}",method = RequestMethod.GET,produces = "application/json")
    public String GetById(@PathVariable String id,HttpServletResponse response){
        Generator generator=random_generator_store.get(id);
        if(generator != null){
            return generator.generate().toString();
        }else{
            response.setStatus(404);
            return null;
        }

    }
    @PostMapping("/avro")
    public String create(@RequestBody Map<String,String> body, HttpServletResponse response){
        String schema=body.get("schema");
        if(schema != null && schema != ""){
            String createdId=storeGenerator(schema);
            if(createdId != null){
                response.setStatus(200);
                return createdId;
            }else{
                response.setStatus(400);
                return "Invalid Schema";
            }
        }else{
            response.setStatus(400);
            return "Bad Request";
        }
    }
    @DeleteMapping("/avro/{id}")
    public String delete(@PathVariable String id,HttpServletResponse response){
        Generator generator= random_generator_store.remove(id);
        if(generator != null){
            return "Deregister success.";
        }else{
            response.setStatus(404);
            return "Id does not found";
        }
    }
    private String storeGenerator(String schema) {
        try {
            Random random = new Random();
            Generator generator = new Generator(schema,random);
            String id=GenerateUUID();
            random_generator_store.put(id, generator);
            return id;
        }catch(Exception e) {
            return null;
            //System.exit(1);
        }
    }
    protected  String GenerateUUID() {
        UUID uuid=UUID.randomUUID();
        String uuidString=uuid.toString();
        return uuidString;
    }
}

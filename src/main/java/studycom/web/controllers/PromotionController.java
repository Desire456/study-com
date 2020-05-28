package studycom.web.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import studycom.web.domain.UsersPart.Promotion;
import studycom.web.domain.UsersPart.User;
import studycom.web.repos.PromotionRepository;

@SessionAttributes(value = "user")
@Controller
public class PromotionController {
    @Autowired
    PromotionRepository promotionRepository;

    @GetMapping("/addPromotion")
    public ModelAndView addPromotion(@ModelAttribute("user") User user, @RequestParam(value = "cause") String cause) {
        String[] causeAndExp = cause.split(" ");
        int expNumber = Integer.parseInt(causeAndExp[1]);
        String senderName = user.getSurname() + " " + user.getName();
        promotionRepository.save(new Promotion(expNumber, senderName, causeAndExp[0], user.getGroup(), user.getId()));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/profile");
        return modelAndView;
    }

    @GetMapping("/processPromotions")
    public ModelAndView processPromotions(@ModelAttribute("user") User user, @RequestParam(value = "promotions")
            String promotions) {
        JSONObject object = new JSONObject(promotions);
        JSONArray jPromotions = object.getJSONArray("promotions");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");
        for (int i = 0; i < jPromotions.length(); ++i) {
            JSONObject jPromotion = jPromotions.getJSONObject(i);
            int promoterId = Integer.parseInt(jPromotion.getString("promoterId"));
            int expNumber = Integer.parseInt(jPromotion.getString("expNumber"));
            int promotionId = Integer.parseInt(jPromotion.getString("id"));
            boolean access = jPromotion.getString("access").equals("yes");
        }
        return modelAndView;
    }
}

package in.eightfolds.paytm_test;

import java.util.Enumeration;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paytm.merchant.CheckSumServiceHelper;

@Controller
public class HomeController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {

		model.addAttribute("randomInt", new Random().nextInt(1000000));
		model.addAttribute("industryTypeId", PaytmConstants.INDUSTRY_TYPE_ID);
		model.addAttribute("chanelId", PaytmConstants.CHANNEL_ID);

		return "home";
	}

	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public String pay(Model model, @RequestParam("ORDER_ID") String orderId,
			@RequestParam("CUST_ID") String custId,
			@RequestParam("INDUSTRY_TYPE_ID") String industryTypeId,
			@RequestParam("CHANNEL_ID") String chanelId,
			@RequestParam("TXN_AMOUNT") String txnAmt) throws Exception {

		TreeMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("REQUEST_TYPE","DEFAULT");
		parameters.put("MID", PaytmConstants.MID);
		parameters.put("ORDER_ID", orderId);
		parameters.put("CUST_ID", custId);
		parameters.put("TXN_AMOUNT", txnAmt);
		parameters.put("CHANNEL_ID", PaytmConstants.CHANNEL_ID == null ? PaytmConstants.CHANNEL_ID : chanelId);
		parameters.put("INDUSTRY_TYPE_ID", industryTypeId == null ? PaytmConstants.INDUSTRY_TYPE_ID : industryTypeId);
		parameters.put("WEBSITE", PaytmConstants.WEBSITE);
		parameters.put("MOBILE_NO", "7777777777");
		parameters.put("EMAIL", "test@gmail.com");
		parameters.put("CALLBACK_URL", "http://localhost:8080/paytm_test/resp");

		String checksumHash = CheckSumServiceHelper.getCheckSumServiceHelper()
				.genrateCheckSum(PaytmConstants.MERCHANT_KEY, parameters);

		model.addAttribute("requestType", parameters.get("REQUEST_TYPE"));
		model.addAttribute("orderId", parameters.get("ORDER_ID"));
		model.addAttribute("custId", parameters.get("CUST_ID"));
		model.addAttribute("chanelId", parameters.get("CHANNEL_ID"));
		model.addAttribute("industryTypeId", parameters.get("INDUSTRY_TYPE_ID"));
		model.addAttribute("mid", parameters.get("MID"));
		model.addAttribute("website", parameters.get("WEBSITE"));
		model.addAttribute("mobileNo", parameters.get("MOBILE_NO"));
		model.addAttribute("email", parameters.get("EMAIL"));
		model.addAttribute("txnAmt", parameters.get("TXN_AMOUNT"));
		model.addAttribute("callbackUrl", parameters.get("CALLBACK_URL"));
		model.addAttribute("CHECKSUMHASH", checksumHash);

		model.addAttribute("paytmUrl", PaytmConstants.PAYTM_URL);

		return "pay";
	}

	@RequestMapping(value = "/resp", method = RequestMethod.POST)
	@ResponseBody
	public String resp(Model model, ServletRequest request) throws Exception {
		Enumeration<String> paramNames = request.getParameterNames();

		Map<String, String[]> mapData = request.getParameterMap();
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		String paytmChecksum = "";
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if (paramName.equals("CHECKSUMHASH")) {
				paytmChecksum = mapData.get(paramName)[0];
			} else {
				parameters.put(paramName, mapData.get(paramName)[0]);
			}
		}
		
		boolean isValideChecksum = false;
		String outputHTML = "";
		
		try {
			isValideChecksum = CheckSumServiceHelper.getCheckSumServiceHelper()
					.verifycheckSum(PaytmConstants.MERCHANT_KEY, parameters,
							paytmChecksum);
			if (isValideChecksum && parameters.containsKey("RESPCODE")) {
				if (parameters.get("RESPCODE").equals("01")) {
					outputHTML = parameters.toString();
				} else {
					outputHTML = "<b>Payment Failed.</b>";
				}
			} else {
				outputHTML = "<b>Checksum mismatched.</b>";
			}
		} catch (Exception e) {
			outputHTML = e.toString();
		}

		return outputHTML;
	}

}

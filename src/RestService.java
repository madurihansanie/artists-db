package lk.dialog.ist.pms.rest;

import java.awt.Menu;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import lk.dialog.ist.pms.client.dto.ApplicationDTO;
import lk.dialog.ist.pms.client.dto.BusinessDTO;
import lk.dialog.ist.pms.client.dto.DeviceDTO;
import lk.dialog.ist.pms.client.dto.DeviceLocationInfoDTO;
import lk.dialog.ist.pms.client.dto.FeatureDTO;
import lk.dialog.ist.pms.client.dto.FunctionDTO;
import lk.dialog.ist.pms.client.dto.LoyaltyBebefitDTO;
import lk.dialog.ist.pms.client.dto.PartnerAddressDTO;
import lk.dialog.ist.pms.client.dto.PartnerProfileDTO;
import lk.dialog.ist.pms.client.dto.PartnerProfileSummaryDTO;
import lk.dialog.ist.pms.client.dto.PointPrizeDTO;
import lk.dialog.ist.pms.client.dto.ProfileDayPointsDTO;
import lk.dialog.ist.pms.client.dto.ProfileDayPointsSummryDTO;
import lk.dialog.ist.pms.client.dto.ProfileDistanceDTO;
import lk.dialog.ist.pms.client.dto.ProfileIncomeStatCommissionDTO;
import lk.dialog.ist.pms.client.dto.ProfileIncomeStatsTotalDTO;
import lk.dialog.ist.pms.client.dto.ProfileLoyaltyCategoryDTO;
import lk.dialog.ist.pms.client.dto.ProfilePointsDTO;
import lk.dialog.ist.pms.client.dto.ProfileRatingDTO;
import lk.dialog.ist.pms.client.dto.ServiceDTO;
import lk.dialog.ist.pms.client.exception.PMSException;
import lk.dialog.ist.pms.client.exception.ProfilePointsNotSufficientException;
import lk.dialog.ist.pms.client.service.BlockedMenuListService;
import lk.dialog.ist.pms.client.service.PartnerManagementService;
import lk.dialog.ist.pms.client.service.RetailerIncomeStatementService;
import lk.dialog.ist.pms.client.service.RetailerLoyaltyService;
import lk.dialog.ist.pms.client.service.RetailerPointsService;
import lk.dialog.ist.pms.client.service.RetailerRatingService;
import lk.dialog.ist.pms.client.service.TradeMarketingService;
import lk.dialog.ist.pms.entity.BlockedMapping;
import lk.dialog.ist.pms.entity.MenuList;
import lk.dialog.ist.pms.entity.PaidModeType;
import lk.dialog.ist.pms.entity.PartnerProfile;
import lk.dialog.ist.pms.rest.request.AgentLocationInfoUploadRequest;
import lk.dialog.ist.pms.rest.request.AgentMonthlyCommissionDetailsRequest;
import lk.dialog.ist.pms.rest.request.AgentPerformanceDetailsRequest;
import lk.dialog.ist.pms.rest.request.BlockedMenuListRequest;
import lk.dialog.ist.pms.rest.request.CreateProfileRequest;
import lk.dialog.ist.pms.rest.request.EarnedPointDetailsRequest;
import lk.dialog.ist.pms.rest.request.GetProfileInformationRequest;
import lk.dialog.ist.pms.rest.request.GetProfilesCreatedByUserRequest;
import lk.dialog.ist.pms.rest.request.LoyaltyBenefitsRequest;
import lk.dialog.ist.pms.rest.request.PointsToReloadTransferRequest;
import lk.dialog.ist.pms.rest.request.RedeemPrizeRequest;
import lk.dialog.ist.pms.rest.response.AgentAddressDetailsResponse;
import lk.dialog.ist.pms.rest.response.AgentLocationInfoUploadResponse;
import lk.dialog.ist.pms.rest.response.AgentMonthlyCommissionDetailsResponse;
import lk.dialog.ist.pms.rest.response.AgentPerformanceDetailsResponse;
import lk.dialog.ist.pms.rest.response.BlockedListResponse;
import lk.dialog.ist.pms.rest.response.CommissionDetail;
import lk.dialog.ist.pms.rest.response.CreateProfileResponse;
import lk.dialog.ist.pms.rest.response.DOOHBaseDataResponse;
import lk.dialog.ist.pms.rest.response.DateWisePointResponse;
import lk.dialog.ist.pms.rest.response.GetProfileInformationResponse;
import lk.dialog.ist.pms.rest.response.GetProfilesCreatedByUserCountResponse;
import lk.dialog.ist.pms.rest.response.GetProfilesCreatedByUserResponse;
import lk.dialog.ist.pms.rest.response.IsDeviceNoExistResponse;
import lk.dialog.ist.pms.rest.response.IsNICNumberExistResponse;
import lk.dialog.ist.pms.rest.response.LoyaltyBenefitsResponse;
import lk.dialog.ist.pms.rest.response.MonthlyCommisionDetail;
import lk.dialog.ist.pms.rest.response.MonthlyTotalCommision;
import lk.dialog.ist.pms.rest.response.Operator;
import lk.dialog.ist.pms.rest.response.PointsToReloadTransferResponse;
import lk.dialog.ist.pms.rest.response.PostalCodeLikeResponse;
import lk.dialog.ist.pms.rest.response.PrizeDetail;
import lk.dialog.ist.pms.rest.response.PrizeListReponse;
import lk.dialog.ist.pms.rest.response.RatingDetail;
import lk.dialog.ist.pms.rest.response.RedeemPrizeResponse;
import lk.dialog.ist.pms.rest.response.Response;
import lk.dialog.ist.pms.rest.response.TradeBoardCategory;
import lk.dialog.ist.pms.rest.response.Vendor;
import lk.dialog.ist.pms.tm.client.dto.LobTypeObjectDTO;
import lk.dialog.ist.pms.tm.client.dto.OperatorBoardImageDTO;
import lk.dialog.ist.pms.tm.client.dto.OperatorBoardInfoDTO;
import lk.dialog.ist.pms.tm.client.dto.OperatorDTO;
import lk.dialog.ist.pms.tm.client.dto.PaidModeTypeObjectDTO;
import lk.dialog.ist.pms.tm.client.dto.TradeBoardCategoryDTO;
import lk.dialog.ist.pms.tm.client.dto.VendorDTO;
import lk.dialog.ist.pms.tm.client.service.DialogOutOfHomeService;
import lk.dialog.ist.pms.tm.rest.request.OperatorBoardInfoUploadRequest;
import lk.dialog.ist.pms.tm.rest.response.OperatorBoardInfoUploadResponse;
import lk.dialog.ist.pms.web.common.ErrorInfor;
import lk.dialog.ist.pms.ws.AddressInformation;
import lk.dialog.ist.pms.ws.AgentSpecialInformation;
import lk.dialog.ist.pms.ws.Application;
import lk.dialog.ist.pms.ws.BusinessFunction;
import lk.dialog.ist.pms.ws.ConsumerApp;
import lk.dialog.ist.pms.ws.ContactInformation;
import lk.dialog.ist.pms.ws.DevicesInfo;
import lk.dialog.ist.pms.ws.DistrictInfo;
import lk.dialog.ist.pms.ws.Feature;
import lk.dialog.ist.pms.ws.IdentificationType;
import lk.dialog.ist.pms.ws.LocationInfo;
import lk.dialog.ist.pms.ws.NatureOfBusiness;
import lk.dialog.ist.pms.ws.PostalCodeInfo;
import lk.dialog.ist.pms.ws.ProfileDistanceInfo;
import lk.dialog.ist.pms.ws.ProfileInformation;
import lk.dialog.ist.pms.ws.ProvinceInfo;
import lk.dialog.ist.pms.ws.SpecialService;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.dialect.FrontBaseDialect;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author kanchana_JKCS
 *
 */
@RestController
public class PMSRestService {
	
	//get log4j handler
	private static final Logger logger = Logger.getLogger(PMSRestService.class);
	
	@Autowired
	private MessageSource msgSource;
		
	@Autowired
	RetailerPointsService retailerPointsService;	
	
	@Autowired
	RetailerRatingService retailerRatingService;
	
	@Autowired
	RetailerLoyaltyService retailerLoyaltyService;
	
	@Autowired
	RetailerIncomeStatementService retailerIncomeStatementService;
	
	@Autowired
	PartnerManagementService partnerManagementService;
	
	@Autowired
	TradeMarketingService tradeMarketingService;
	
	@Autowired
	DialogOutOfHomeService dialogOutOfHomeService;	
	
	@Autowired
	BlockedMenuListService blockedMenuListService;

	@Value("${max.image.size}")
	private Long maximumImageSize;
	
	private Locale currentLocale = LocaleContextHolder.getLocale();
		
	@RequestMapping(value = "api/getAgentPoints", method = RequestMethod.POST)
	public @ResponseBody
	AgentPerformanceDetailsResponse getAgentPoints(
			@RequestBody AgentPerformanceDetailsRequest request) {
		AgentPerformanceDetailsResponse response = new AgentPerformanceDetailsResponse();
		response.setMobileNo(request.getMobileNo());
		
		try {
			ProfilePointsDTO dto = retailerPointsService.getProfilePoints(request.getMobileNo());
			if (dto != null) {
				response.setTotalPoints(dto.getPointsAvailable());
			}
			else {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc(Response.ERR_POINTS_NOT_FOUND);
			}
		} catch (Exception e) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(e.getMessage());
			e.printStackTrace();
		}		
		
		return response;
	}
	
	@RequestMapping(value = "api/getDateWisePoints", method = RequestMethod.POST)
	public @ResponseBody DateWisePointResponse getDateWisePoints(@RequestBody EarnedPointDetailsRequest request) {
		DateWisePointResponse response = new DateWisePointResponse();
		Date endDate = null;
		Date validDate = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date startDate = formatter.parse(request.getStartDate());
			endDate = formatter.parse(request.getEndDate());
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH)+6));
			validDate = cal.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if ((request.getNic() ==null || request.getNic().equals("")) && (request.getPmsId() == null || request.getPmsId().equals(""))) {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("Mandatory field required");
			}else if(request.getStartDate() == null || request.getStartDate().equals("") || request.getEndDate() ==null || request.getEndDate().equals("") ) {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("Mandatory field required");
			}
			else if (!request.getPmsId().equals("") && !request.getPmsId().matches("[0-9]+")) {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("Invalid PMS ID");
			}else if (endDate.after(validDate)){
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("Date range should not be more than 6 months");
			}
			else {
				ProfileDayPointsSummryDTO dayPointsSummryDTO = retailerPointsService.getProfileDayPointsForDuration(
						request.getDeviceID(), request.getNic(), request.getPmsId(), request.getStartDate(),
						request.getEndDate());
				if (dayPointsSummryDTO != null) {
					if (dayPointsSummryDTO.getProfileId() != null) {
						try {
							ProfilePointsDTO dto = retailerPointsService
									.getProfilePointsbyProfileID(dayPointsSummryDTO.getProfileId());
							if (dto != null) {
								response.setStatus(Response.TX_SUCCESS);
								response.setTotalPointsEarned(dto.getPointsEarned());
								response.setTotalPointsRedeemed(dto.getPointsRedeemed());
								response.setBalancedPoints(dto.getPointsAvailable());
								response.setProfileDayPoints(dayPointsSummryDTO.getProfileDayPoints());
								response.setProfileId(dayPointsSummryDTO.getProfileId());
							} else {
								response.setStatus(Response.TX_ERROR);
								response.setErrorDesc(Response.ERR_POINTS_NOT_FOUND);
							}
						} catch (Exception e) {
							response.setStatus(Response.TX_ERROR);
							response.setErrorDesc(e.getMessage());
							e.printStackTrace();
						}
					} else {
						response.setStatus(Response.TX_ERROR);
						response.setErrorDesc(Response.ERR_POINTS_NOT_FOUND);
					}

				} else {
					response.setStatus(Response.TX_ERROR);
					response.setErrorDesc(Response.ERR_POINTS_NOT_FOUND);
				}
			}
		} catch (Exception e) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(e.getMessage());
			e.printStackTrace();
		}

		return response;
	}
	
	@RequestMapping(value = "api/getBlockedMenuList", method = RequestMethod.POST)
	public @ResponseBody BlockedListResponse getBlockedMenuList(@RequestBody BlockedMenuListRequest request) {
		BlockedListResponse response = new BlockedListResponse();
		logger.info("api/getBlockedMenuList API call started." );
		logger.info("Request: " +request.toString());
		int identificationType =0;
		try {
			if(request.getIdentification_type() == null || request.getIdentification_type().equals("")) {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("Mandatory field required for Identification_type");
				logger.error("Mandatory field required for Identification_type ");
			}else if (!request.getIdentification_type().equalsIgnoreCase("pmsId") && !request.getIdentification_type().equalsIgnoreCase("deviceNo")) {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("Invalid identification_type");
				logger.error("Invalid Identification_type :" + request.getIdentification_type());
			}
			else if(request.getIdentification_type().equalsIgnoreCase("pmsId")) {
				if(request.getIdentification_value().equals("") || request.getIdentification_value() == null) {
					response.setStatus(Response.TX_ERROR);
					response.setErrorDesc("Mandatory field required for Identification_value");
					logger.error("Mandatory field required for Identification_value ");
				}
				else if (!request.getIdentification_value().matches("[0-9]+")) {
					response.setStatus(Response.TX_ERROR);
					response.setErrorDesc("Invalid identification_value");
					logger.error("Invalid Identification_value : " + request.getIdentification_value());
				}
				identificationType =1;
			}
			else if(request.getIdentification_type().equalsIgnoreCase("deviceNo")) {
				if(request.getIdentification_value().equals("") || request.getIdentification_value() == null) {
					response.setStatus(Response.TX_ERROR);
					response.setErrorDesc("Mandatory field required");
					logger.error("Mandatory field required for getIdentification_value ");
				}
				else if (!request.getIdentification_value().matches("[0-9]+")) {
					response.setStatus(Response.TX_ERROR);
					response.setErrorDesc("Invalid identification_value");
					logger.error("Invalid Identification_value : " + request.getIdentification_value());
				}
				identificationType =2;
			}
			if(request.getChannel() ==null || request.getChannel().equals("") ) {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("Mandatory field required");
				logger.error("Mandatory field required for Channel ");
			}else if(request.getLob() == null || request.getLob().equals("")) {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("Mandatory field required");
				logger.error("Mandatory field required for Lob ");
			}else if (request.getPaidMode() == null || request.getPaidMode().equals("")) {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("Mandatory field required");
				logger.error("Mandatory field required for PaidMode ");
			}else if (request.getAppCode() == null || request.getAppCode().equals("")) {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("Mandatory field required");
				logger.error("Mandatory field required for AppCode ");
			} else {
				try {
					String deviceStr = null;
					Long pmsId = null;
					if (identificationType == 1) {
						pmsId = Long.parseLong(request.getIdentification_value());
						deviceStr = blockedMenuListService.getDevices(pmsId);
					} else if (identificationType == 2) {
						deviceStr = request.getIdentification_value();
						pmsId = blockedMenuListService.getPmsID(deviceStr);
					}
					if (pmsId != 0 && !deviceStr.trim().equals("")) {
						List<LobTypeObjectDTO> lobTypeObjectDTOs = blockedMenuListService.getBlockedMenuList(pmsId,request.getChannel(), request.getLob(), request.getPaidMode(), request.getAppCode());
						if (lobTypeObjectDTOs != null && lobTypeObjectDTOs.size() != 0) {
							response.setStatus(Response.TX_SUCCESS);
							response.setProfileId(pmsId.toString());
							response.setDeviceNo(deviceStr);
							response.setChannel(request.getChannel());
							response.setAppCode(request.getAppCode());
							response.setRestricted(lobTypeObjectDTOs);
						}else {
							response.setStatus(Response.TX_SUCCESS);
							response.setErrorDesc("Records Not found");
							logger.error("Records Not found");
						}
					}else {
						response.setStatus(Response.TX_FAILED);
						response.setErrorDesc("Agent not found");
						logger.error("Agent not found");
					}

				} catch (Exception e) {
					response.setStatus(Response.TX_ERROR);
					response.setErrorDesc(e.getMessage());
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(e.getMessage());
			e.printStackTrace();
		}

		return response;
	}
	
	@RequestMapping(value = "api/getAgentLoyaltyCategory", method = RequestMethod.POST)
	public @ResponseBody
	AgentPerformanceDetailsResponse getAgentLoyaltyCategory(
			@RequestBody AgentPerformanceDetailsRequest request) {
		AgentPerformanceDetailsResponse response = new AgentPerformanceDetailsResponse();
		response.setMobileNo(request.getMobileNo());
		
		try {
			
			ProfileLoyaltyCategoryDTO dto =  retailerLoyaltyService.getAgentLoyaltyCategory(request.getMobileNo());
			if (dto != null) {
				response.setLoyaltyCategory(dto.getLoyaltyCategory());
			}
			else {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc(Response.ERR_LOYALTY_CAT_NOT_FOUND);
			}
		} catch (Exception e) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(e.getMessage());
		}
		
		return response;
	}
	
	@RequestMapping(value = "api/getAgentRating", method = RequestMethod.POST)
	public @ResponseBody
	AgentPerformanceDetailsResponse getAgentRating(
			@RequestBody AgentPerformanceDetailsRequest request) {
		AgentPerformanceDetailsResponse response = new AgentPerformanceDetailsResponse();
		response.setMobileNo(request.getMobileNo());
		List<RatingDetail> ratingDetails = new ArrayList<RatingDetail>();
		
		try {
			List<ProfileRatingDTO> profileRatings = retailerRatingService.getRetailerRatings(request.getMobileNo());
			if (profileRatings != null && !profileRatings.isEmpty()) {
				for (ProfileRatingDTO dto : profileRatings) {
					RatingDetail rating = new RatingDetail(dto.getRatingType(), dto.getRatingLevel());
					ratingDetails.add(rating);
				}
				response.setRatingDetails(ratingDetails);
			}
			else {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc(Response.ERR_RATING_NOT_FOUND);
			}
			
		} catch (Exception e) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(e.getMessage());
		}
		
		return response;
	}
	
	@RequestMapping(value = "api/getPrizeList", method = RequestMethod.GET)
	public @ResponseBody
	PrizeListReponse getPrizeList() {
		PrizeListReponse response = new PrizeListReponse();
		List<PrizeDetail> prizeDetails = new ArrayList<>();
		
		try {
			List<PointPrizeDTO> prizes = retailerPointsService.getAllPointPrizes();
			
			for (PointPrizeDTO dto : prizes) {
				PrizeDetail prizeDetail = new PrizeDetail(dto.getPrizeCode(), dto.getPrizeDesc(), dto.getRedeemPoints());
				prizeDetails.add(prizeDetail);
			}
			response.setPrizeDetails(prizeDetails);
		} catch (Exception e) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(e.getMessage());
		}		
		
		return response;
	}
	
	@RequestMapping(value = "api/getMonthlyTotalCommissions", method = RequestMethod.POST)
	public @ResponseBody
	AgentMonthlyCommissionDetailsResponse getMonthlyTotalCommissions(@RequestBody AgentMonthlyCommissionDetailsRequest request) {
		Date dateFrom = null;
		Date dateTo = null;
		AgentMonthlyCommissionDetailsResponse response = new AgentMonthlyCommissionDetailsResponse();
		response.setMobileNo(request.getMobileNo());
		
		if (request.getDateFrom() == null || request.getDateTo() == null) {
			Calendar calFrom = Calendar.getInstance();
			calFrom.add(Calendar.MONTH, -3);
			calFrom.set(Calendar.DAY_OF_MONTH, 1);
			dateFrom = getStartOfDate(calFrom.getTime());
			Calendar calTo = Calendar.getInstance();
			calTo.add(Calendar.MONTH, -1);
			calTo.set(Calendar.DAY_OF_MONTH, calTo.getActualMaximum(Calendar.DAY_OF_MONTH));
			dateTo = getEndOfDate(calTo.getTime()); 			
		}
		else {			
			dateFrom = getStartOfDate(request.getDateFrom());			
			dateTo = getEndOfDate(request.getDateTo());
		}
		try {
			List<ProfileIncomeStatsTotalDTO> monthlyTotals = retailerIncomeStatementService.getProfileIncomeStatsMonthlyTotals(request.getMobileNo(), 
					dateFrom, dateTo);
			
			if (monthlyTotals != null && !monthlyTotals.isEmpty()) {
				
				for (ProfileIncomeStatsTotalDTO dto : monthlyTotals) {
					response.addMonthlyTotalCommission(new MonthlyTotalCommision(Integer.parseInt(dto.getCommissionYearString()),
							dto.getCommissionMonthString(), dto.getCommission()));
				}
			}
			else {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc(Response.ERR_INCOME_STAT_NOT_FOUND);
			}
		} catch (Exception e) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(e.getMessage());
		}
		
		return response;
	}
	
	@RequestMapping(value = "api/getMonthlyCommissionDetails", method = RequestMethod.POST)
	public @ResponseBody
	AgentMonthlyCommissionDetailsResponse getMonthlyCommissionDetails(
			@RequestBody AgentMonthlyCommissionDetailsRequest request) {
		Date dateFrom = null;
		Date dateTo = null;
		Date month = null;
		MonthlyCommisionDetail detail =null;;
		AgentMonthlyCommissionDetailsResponse response = new AgentMonthlyCommissionDetailsResponse();
		response.setMobileNo(request.getMobileNo());
		
		if (request.getDateFrom() == null || request.getDateTo() == null) {
			Calendar calFrom = Calendar.getInstance();
			calFrom.add(Calendar.MONTH, -3);
			calFrom.set(Calendar.DAY_OF_MONTH, 1);
			dateFrom = getStartOfDate(calFrom.getTime());
			Calendar calTo = Calendar.getInstance();
			calTo.add(Calendar.MONTH, -1);
			calTo.set(Calendar.DAY_OF_MONTH, calTo.getActualMaximum(Calendar.DAY_OF_MONTH));
			dateTo = getEndOfDate(calTo.getTime()); 			
		}
		else {			
			dateFrom = getStartOfDate(request.getDateFrom());			
			dateTo = getEndOfDate(request.getDateTo());
		}
		
		try {
			List<ProfileIncomeStatCommissionDTO> commissions = retailerIncomeStatementService.getProfileIncomeStatCommissionsforMonth(
					request.getMobileNo(), dateFrom, dateTo);
			if (commissions != null && !commissions.isEmpty())  {
				
				for (ProfileIncomeStatCommissionDTO dto : commissions) {
					if (!dto.getCommissionMonth().equals(month)) {
						if (detail != null) {
							response.addMonthlyCommisionDetail(detail);
						}						
						month = dto.getCommissionMonth();						
						detail = new MonthlyCommisionDetail();
						detail.setMonth(dto.getCommissionMonthString());
						detail.setYear(Integer.parseInt(dto.getCommissionYearString()));										
					}
					detail.addCommissionDetail(new CommissionDetail(dto.getCommissionType(), dto.getCommission()));					
				}
				response.addMonthlyCommisionDetail(detail);
			}
			else {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc(Response.ERR_INCOME_STAT_NOT_FOUND);
			}
		}catch (Exception e) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(e.getMessage());
		}		
		
		return response;
	}
	
	@RequestMapping(value = "api/redeemPrize", method = RequestMethod.POST)
	public @ResponseBody
	RedeemPrizeResponse redeemPrize(@RequestBody RedeemPrizeRequest request) {
		logger.info("REST redeemPrize mobile : " + request.getMobileNo());
		logger.info("REST redeemPrize prize code : " + request.getPrizeCode());
		
		RedeemPrizeResponse response = new RedeemPrizeResponse();
		response.setMobileNo(request.getMobileNo());
		
		try {
			ProfilePointsDTO points = retailerPointsService.getProfilePoints(request.getMobileNo());			
			if (points != null) {
				PointPrizeDTO prize = retailerPointsService.getPointPrize(request.getPrizeCode());
				
				if (prize != null) {
					/*if (points.getPointsAvailable() < prize.getRedeemPoints()) {
						response.setStatus(Response.TX_ERROR);
						response.setErrorDesc(Response.INSUFFICIENT_POINTS);
					}
					else {
						Integer totalRedemptions = prize.getRedeemPoints() + points.getPointsRedeemed();
						Integer currentBalance = points.getPointsEarned() - totalRedemptions;
						
						points.setPointsRedeemed(totalRedemptions);
						points.setPointsAvailable(currentBalance);
						points.setSourceUpdated("REST");
						points = retailerPointsService.saveProfilePoints(points);
						
						//save profile day points
						ProfileDayPointsDTO profileDayPoints = retailerPointsService.getProfileDayPoints(
								points.getProfileId(), new Date());						
						if (profileDayPoints == null) {
							profileDayPoints = new ProfileDayPointsDTO();
							profileDayPoints.setProfileId(points.getProfileId());
							profileDayPoints.setNoOfPointsEarned(0);
							profileDayPoints.setNoOfPointsRedeemed(0);
						}
						profileDayPoints.setNoOfPointsRedeemed(profileDayPoints.getNoOfPointsRedeemed() 
								+ Math.abs(prize.getRedeemPoints().intValue()));						
						profileDayPoints = retailerPointsService.saveProfileDayPoints(profileDayPoints);
						
						if (points.getId() != null && profileDayPoints.getId() != null) {
							response.setStatus(Response.TX_SUCCESS);
						}	
					}			*/		
					
					try {
						Boolean redeemStatus = retailerPointsService.redeemPrize(points.getProfileId(), prize.getPrizeCode(), "REST", request.getMobileNo());
						if (redeemStatus) {
							response.setStatus(Response.TX_SUCCESS);
						}
						else {
							logger.info("Transaction failed unexpectedly");
							response.setStatus(Response.TX_ERROR);
							response.setErrorDesc(Response.UNEXPECTED_ERROR);
						}
					} catch (ProfilePointsNotSufficientException pnse) {
						logger.info(pnse.getMessage(), pnse);
						response.setStatus(Response.TX_ERROR);
						response.setErrorDesc(Response.INSUFFICIENT_POINTS);
					}
				}
				else {
					logger.info("REST redeemPrize prize not found for prize code : " + request.getPrizeCode() 
							+ ", mobile : " + request.getMobileNo());
					response.setStatus(Response.TX_ERROR);
					response.setErrorDesc(Response.ERR_PRIZE_NOT_FOUND);
				}				
			}
			else {
				logger.info("REST redeemPrize agent not found for mobile : " + request.getMobileNo());
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc(Response.ERR_AGENT_NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Unexpected error", e);
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(e.getMessage());
		}				
		return response;
	}
	
	@RequestMapping(value = "api/getLoyaltyBenefits", method = RequestMethod.POST)
	public @ResponseBody
	LoyaltyBenefitsResponse getLoyaltyBenefits(@RequestBody LoyaltyBenefitsRequest request) {
		
		LoyaltyBenefitsResponse response = new LoyaltyBenefitsResponse();
		response.setMobileNo(request.getMobileNo());
		List<String> benefits = new ArrayList<>();
		
		try {
			List<LoyaltyBebefitDTO> loyaltyBenefits = retailerLoyaltyService.getLoyaltyBenefits(request.getLoyaltyCategory());
			for (LoyaltyBebefitDTO dto : loyaltyBenefits) {
				benefits.add(dto.getLoyaltyBenefit());
			}
			response.setBenefits(benefits);
		} catch (Exception e) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(e.getMessage());
		}				
		return response;
	}	
	
	@RequestMapping(value = "api/getAgentAddressDetails", method = RequestMethod.POST)
	public @ResponseBody
	AgentAddressDetailsResponse getAgentAddressDetails(@RequestBody AgentPerformanceDetailsRequest request) {
		
		AgentAddressDetailsResponse response = new AgentAddressDetailsResponse();
		response.setMobileNo(request.getMobileNo());		
		
		try {
			PartnerAddressDTO addressDTO = partnerManagementService.getProfileAddress(request.getMobileNo());
			if (addressDTO != null) {
				response.setAddressNo(addressDTO.getAddressNo());
				response.setAddressLine1(addressDTO.getAddressLine1());
				response.setAddressLine2(addressDTO.getAddressLine2());
				response.setAddressLine3(addressDTO.getAddressLine3());
				if (addressDTO.getPostal() != null) {
					response.setPostalCode(addressDTO.getPostal().getAgentPostalCode());
				}				
				response.setDistrict(addressDTO.getDistrict().getDistrict());
				response.setProvince(addressDTO.getProvince().getProvince());
				response.setDsDivision(addressDTO.getDsDivision().getDsDivision());
			}
			else {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc(Response.ERR_AGENT_NOT_FOUND);
			}			
		} catch (Exception e) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(e.getMessage());
		}				
		return response;
	}	
	
	private Date getStartOfDate(Date date)  {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);
		Calendar cal = Calendar.getInstance();
		cal.set(calDate.get(Calendar.YEAR), calDate.get(Calendar.MONTH), calDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	private Date getEndOfDate(Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);
		Calendar cal = Calendar.getInstance();
		cal.set(calDate.get(Calendar.YEAR), calDate.get(Calendar.MONTH), calDate.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	@RequestMapping(value = "api/pointsToReloadTransfer", method = RequestMethod.POST)
	public @ResponseBody
	PointsToReloadTransferResponse pointsToReloadTransfer(@RequestBody PointsToReloadTransferRequest request) {
		
		System.out.println("Amount : " + request.getAmount());
		
		PointsToReloadTransferResponse response = new PointsToReloadTransferResponse();
		response.setTransactionID(0);
		return response;
	}
	
	@RequestMapping(value = "api/getDOOHBaseData", method = RequestMethod.GET)
	public @ResponseBody
	DOOHBaseDataResponse getDOOHBaseData() {
		DOOHBaseDataResponse response = new DOOHBaseDataResponse();
		
		List<OperatorDTO> operators = dialogOutOfHomeService.getAllOperators();
		for (OperatorDTO dto : operators) {
			if (dto.getStatus().equals("ACTIVE")) {
				Operator operator = new Operator(dto.getId(), dto.getOperatorName());
				response.addOperator(operator);
			}			
		}
		
		List<VendorDTO> vendors = dialogOutOfHomeService.getAllVendors();
		for (VendorDTO dto : vendors) {
			if (dto.getStatus().equals("ACTIVE")) {
				Vendor vendor = new Vendor(dto.getId(), dto.getVendorName(), dto.getVendorCode(), dto.getSapCode());
				response.addVendor(vendor);
			}
		}
		
		List<TradeBoardCategoryDTO> categories = dialogOutOfHomeService.getAllTradeBoardCategories();
		for (TradeBoardCategoryDTO dto : categories) {
			if (dto.getStatus().equals("ACTIVE")) {
				TradeBoardCategory category = new TradeBoardCategory(dto.getId(), dto.getCategoryName());
				response.addTradeBoardCategory(category);
			}
		}
		
		return response;
	}
	
	@RequestMapping(value = "api/agentLocationInfoUpload", method = RequestMethod.POST)
	public @ResponseBody
	AgentLocationInfoUploadResponse agentLocationInfoUpload(@RequestBody AgentLocationInfoUploadRequest request) {
		
		AgentLocationInfoUploadResponse response = new AgentLocationInfoUploadResponse();
		String imageLocation = null;
		String frontImage = null;
		String image1 = null;
		String image2 = null;
		String image3 = null;
		String image4 = null;
		PartnerProfileDTO profile = null;		
		
		logger.info("MSISDN : " + request.getmSISDN());
		
		if (request.getmSISDN() == null) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc("mSISDN is mandatory");
			return response;
		}
		else {
			profile = partnerManagementService.getProfileByDeviceNo(request.getmSISDN().trim());
			if (profile == null) {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("Profile not found for mSISDN " + request.getmSISDN());
				return response;
			}
		}
		
		if (request.getLongitude() == null) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc("longitude is mandatory");
			return response;
		}
		
		if (request.getLatitude() == null) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc("latitude is mandatory");
			return response;
		}
		
		if (request.getFrontImage() == null) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc("frontImage is mandatory");
			return response;
		}
		
		// get image location
		try {

			imageLocation = getTMImageLocation();
		} catch (NamingException ne) {
			logger.error(
					"Exception from PMSRestServcie.agentLocationInfoUpload() method\n",
					ne);	
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(ne.getMessage());
			return response;
		}		
		
		//Save front image
		try {
			
			String fileType = request.getFrontImage().getContentType()
					.substring(0, 5);
			if (fileType.equalsIgnoreCase("image")) {
				saveImageFile(request.getFrontImage(), imageLocation);
				frontImage = request.getFrontImage().getOriginalFilename();
			} else {				
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("Failed to save front Image");
				response.addFailedImage(request.getFrontImage());
				return response;
			}								
						
		} catch (IllegalStateException illegalStateException) {
			logger.error(
					"Exception from PMSRestService.saveImgeFile() method\n",
					illegalStateException);
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(illegalStateException.getMessage());	
			return response;

		} catch (IOException ioException) {
			logger.error(
					"Exception from PMSRestService.saveImgeFile() method\n",
					ioException);
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(ioException.getMessage());
			return response;
		}
		
		// Save other images
		if (request.getOtherImages() != null && !request.getOtherImages().isEmpty()) {
			int i = 0;
			for (MultipartFile image : request.getOtherImages()) {
				i++;
				if (image != null) {
					String fileType = image.getContentType()
							.substring(0, 5);
					if (fileType.equalsIgnoreCase("image")) {
						try {
							saveImageFile(image, imageLocation);						
							switch (i) {
	                    		case 1:
	                    			image1 = image.getOriginalFilename();
	                    		case 2:
	                    			image2 = image.getOriginalFilename();
	                    		case 3:
	                    			image3 = image.getOriginalFilename();
	                    		case 4:
	                    			image4 = image.getOriginalFilename();	                    		
							}							
							} catch (IllegalStateException illegalStateException) {
							logger.error(
									"Exception from PMSRestService.saveImgeFile() method\n",
									illegalStateException);
							response.setStatus(Response.TX_ERROR);
							response.setErrorDesc(illegalStateException.getMessage());	
							return response;
	
						} catch (IOException ioException) {
							logger.error(
									"Exception from PMSRestService.saveImgeFile() method\n",
									ioException);
							response.setStatus(Response.TX_ERROR);
							response.setErrorDesc(ioException.getMessage());
							return response;
						}				
									
					} else {
						response.addFailedImage(image);
					}
				}
			}
		}
			
		if (response.getFailedImages() != null && !response.getFailedImages().isEmpty()) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc("Failed to save images");
			return response;
		}
		
		// Save agent device location info.
		try {
			DeviceLocationInfoDTO deviceLocationInfoDto = new  DeviceLocationInfoDTO();
			//deviceLocationInfoDto.setProfileId(profile.getProfileId());
			deviceLocationInfoDto.setDeviceNo(request.getmSISDN());
			deviceLocationInfoDto.setAgentName(request.getAgentName());
			deviceLocationInfoDto.setLongitude(request.getLongitude());
			deviceLocationInfoDto.setLatitude(request.getLatitude());
			deviceLocationInfoDto.setFrontImageName(frontImage);
			deviceLocationInfoDto.setOtherImage1Name(image1);
			deviceLocationInfoDto.setOtherImage2Name(image2);
			deviceLocationInfoDto.setOtherImage3Name(image3);
			deviceLocationInfoDto.setOtherImage4Name(image4);
			deviceLocationInfoDto.setCreatedModifiedUser("REST");
			tradeMarketingService.saveDeviceLocationInfo(deviceLocationInfoDto);
			
		} catch (Exception e) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(e.getMessage());
			return response;			
		}		
		
		return response;
	}
	
	@RequestMapping(value = "api/tm/operatorBoardInfoUpload", method = RequestMethod.POST)
	public @ResponseBody
	OperatorBoardInfoUploadResponse operatorBoardInfoUpload(@RequestBody OperatorBoardInfoUploadRequest request) {
		
		OperatorBoardInfoUploadResponse response = new OperatorBoardInfoUploadResponse();
		String imageLocation = null;
		String archLocation = null;
		OperatorDTO operator = null;
		VendorDTO vendor = null;
		TradeBoardCategoryDTO category = null;
		List<OperatorBoardImageDTO> images = new ArrayList<>();
		String imageCode = "IMAGE_";
		
		if (request.getAdsCode() == null) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc("adsCode is mandatory");
			return response;
		}
		
		if (request.getOperatorId() == null) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc("operator is mandatory");
			return response;
		} else {
			operator = dialogOutOfHomeService.getOperator(request.getOperatorId());
			if (operator == null) {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("operator not found.");
				return response;
			}
		}		
		
		if (request.getVendorId() == null) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc("vendor is mandatory");
			return response;
		} else {
			vendor = dialogOutOfHomeService.getVendor(request.getVendorId());
			if (vendor == null) {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("vendor not found.");
				return response;
			}
		}
		
		if (request.getCategoryId() == null) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc("category is mandatory");
			return response;
		} else {
			category = dialogOutOfHomeService.getTradeBoardCategory(request.getCategoryId());
			if (category == null) {
				response.setStatus(Response.TX_ERROR);
				response.setErrorDesc("category not found.");
				return response;
			}
		}
		
		if (request.getLongitude() == null) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc("longitude is mandatory");
			return response;
		}
		
		if (request.getLatitude() == null) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc("latitude is mandatory");
			return response;
		}
		
		// get image locations
		try {			
			imageLocation = getDOOHImageLocation();
			archLocation = getDOOHArchiveImageLocation();
		} catch (NamingException ne) {
			logger.error(
					"Exception from PMSRestServcie.agentLocationInfoUpload() method\n",
					ne);	
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(ne.getMessage());
			return response;
		}
		
		// Save images
		if (request.getImages() != null && !request.getImages().isEmpty()) {
			int i = 0;
			for (MultipartFile image : request.getImages()) {
				i++;
				if (image != null) {
					String fileType = image.getContentType()
							.substring(0, 5);
					if (fileType.equalsIgnoreCase("image")) {
						try {
							archiveImageFile(image, imageLocation, archLocation);	
							OperatorBoardImageDTO imageDTO = new OperatorBoardImageDTO();
							imageDTO.setImageCode(imageCode + i);
							imageDTO.setImageName(image.getOriginalFilename());
							images.add(imageDTO);
							
						} catch (IllegalStateException illegalStateException) {
							logger.error(
									"Exception from PMSRestService.saveImgeFile() method\n",
									illegalStateException);
							response.setStatus(Response.TX_ERROR);
							response.setErrorDesc(illegalStateException.getMessage());	
							return response;
		
						} catch (IOException ioException) {
							logger.error(
									"Exception from PMSRestService.saveImgeFile() method\n",
									ioException);
							response.setStatus(Response.TX_ERROR);
							response.setErrorDesc(ioException.getMessage());
							return response;
						}
					}
					else {
						response.addFailedImage(image);
					}
				}
			}
		}
		
		if (response.getFailedImages() != null && !response.getFailedImages().isEmpty()) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc("Failed to save images");
			return response;
		}			
			
		try {
			OperatorBoardInfoDTO boardInfoDto = new  OperatorBoardInfoDTO();
			boardInfoDto.setAdsCode(request.getAdsCode());
			boardInfoDto.setCategoryId(category.getId());
			boardInfoDto.setOperatorId(operator.getId());
			boardInfoDto.setVendorId(vendor.getId());
			boardInfoDto.setLongitude(request.getLongitude());
			boardInfoDto.setLatitude(request.getLatitude());
			boardInfoDto.setHeight(request.getHeight());
			boardInfoDto.setWidth(request.getWidth());
			boardInfoDto.setDistrict(request.getDistrict());
			boardInfoDto.setProvice(request.getProvice());
			boardInfoDto.setImages(images);
			dialogOutOfHomeService.uploadLocationData(boardInfoDto);
			
		} catch (Exception e) {
			response.setStatus(Response.TX_ERROR);
			response.setErrorDesc(e.getMessage());
		}	
		
		return response;
	}
	
	private void saveImageFile(MultipartFile mFile, String imageLocation) throws IOException, IllegalStateException {
		String fullImageFilesLocation = imageLocation;
		File imageDirectory = new File(fullImageFilesLocation);

		if (!imageDirectory.exists()) {
			imageDirectory.mkdirs();
		}
		mFile.transferTo(new File(
				fullImageFilesLocation
						+ mFile.getOriginalFilename()));
	}
	
	private void archiveImageFile(MultipartFile mFile, String imageLocation, String archImageLocation) throws IOException {
		
		File imageArchDirectory = new File(archImageLocation);		
		
		File file = new File(imageLocation
				+ mFile.getOriginalFilename());
		if (file.exists()) {
			if (!imageArchDirectory.exists()) {
				imageArchDirectory.mkdir();
			}
			
			logger.info("Duplicate image file upload. File Name: "+ mFile.getOriginalFilename());
			FileUtils.moveFileToDirectory(file, imageArchDirectory, true);
		}
	}
	
	private String getTMImageLocation() throws NamingException {

		Context initContext = new InitialContext();
		String location = (String) initContext
				.lookup("java:/comp/env/imageLocation");
		location = location.replace("\\", "/");
		return location;
	}
	
	private String getDOOHImageLocation() throws NamingException {

		Context initContext = new InitialContext();
		String location = (String) initContext
				.lookup("java:/comp/env/doohImageLocation");
		location = location.replace("\\", "/");
		return location;
	}
	
	private String getDOOHArchiveImageLocation() throws NamingException {

		Context initContext = new InitialContext();
		String location = (String) initContext
				.lookup("java:/comp/env/doohImageArchiLocation");
		location = location.replace("\\", "/");
		return location;
	}
	
	@RequestMapping(value = "api/getProfileInformation", method = RequestMethod.POST)
	public @ResponseBody
	GetProfileInformationResponse getProfileInformation(@RequestBody GetProfileInformationRequest request) {
		logger.info("getProfileInformation|request" + request);
		GetProfileInformationResponse response = new GetProfileInformationResponse();
		
		if (request.getIdentificationType() == null || request.getIdentificationType().isEmpty()) {
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(msgSource.getMessage("identificationtype.null.empty", null, currentLocale));
			return response;
		}
		if (request.getIdentificationValue() == null || request.getIdentificationValue().isEmpty()) {
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(msgSource.getMessage("identificationvalue.null.empty", null, currentLocale));
			return response;
		}
		if (request.getConsumerApp() == null || request.getConsumerApp().isEmpty()) {
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(msgSource.getMessage("consumerapp.null.empty", null, currentLocale));
			return response;
		}
		if (!isValidIdentificationType(request.getIdentificationType())) {
			logger.info("getProfileInformation|request" + request +"|Invalid IdentificationType : " + request.getIdentificationType());
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(msgSource.getMessage("identificationtype.invalid", null, currentLocale));
			return response;
		}
		
		if (!isValidConsumerType(request.getConsumerApp())) {
			logger.info("getProfileInformation|request" + request +"|Invalid consumerApp : " + request.getConsumerApp());
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(msgSource.getMessage("consumerapp.invalid", null, currentLocale));
			return response;
		}
		
		try {
			PartnerProfileDTO dto = null;
			if (request.getIdentificationType().equals(IdentificationType.PROFILE_ID.toString())) {
				logger.info("Profile id : " + request.getIdentificationValue());				
				dto = partnerManagementService.getProfile(new Long(request.getIdentificationValue()), request.getConsumerApp());
			}
			else if (request.getIdentificationType().equals(IdentificationType.NIC.toString())) {
				logger.info("NIC : " + request.getIdentificationValue());
				dto = partnerManagementService.getProfileByNIC(request.getIdentificationValue().trim(), request.getConsumerApp());
			}
			else if (request.getIdentificationType().equals(IdentificationType.DEVICE_NO.toString())) {
				logger.info("Device no  : " + request.getIdentificationValue());
				dto = partnerManagementService.getProfileByDeviceNo(request.getIdentificationValue().trim(), request.getConsumerApp());
			}
			
			if (dto != null) {				
				logger.info("getProfileInformation|Profile found for " + request);
				response.setResponseCode(Response.TX_SUCCESS);
				//response.setProfile(getProfileInformation(dto));	
				response.setProfile(dto);	
			}
			else {
				logger.info("getProfileInformation|Profile NOT found for " + request);
				String[] arr = new String[3];
				arr[0] = request.toString();				
				response.setResponseCode(Response.TX_FAILED);
				response.setResponseDescription(msgSource.getMessage("profile.not.found", arr, currentLocale));				
			}
			return response;
		} catch (Exception e) {
			logger.info("getProfileInformation|Unexpected Error " + e.getMessage());
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(Response.UNEXPECTED_ERROR);
			return response;			
		}			
	}
	
	private boolean isValidIdentificationType(String identificationType) {
		try {
			IdentificationType.valueOf(identificationType);
			return true;
		} catch (IllegalArgumentException e) {
	         return false;
	    }		
	}
	
	private boolean isValidConsumerType(String consumerType) {
		try {
			ConsumerApp.valueOf(consumerType);
			return true;
		} catch (IllegalArgumentException e) {
	         return false;
	    }
	}
	
	@RequestMapping(value = "api/createProfile", method = RequestMethod.POST)
	public @ResponseBody
	CreateProfileResponse createProfile(@RequestBody CreateProfileRequest request) {
		
		long startTime = System.currentTimeMillis();
		
		CreateProfileResponse response =  new CreateProfileResponse();		
		
		logger.info("createProfile|REST|START|");
		
		if (request.getProfile() == null) {
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(Response.PROFILE_IS_NULL);
			return response;
		}
		
		try {
			PartnerProfileDTO profile = partnerManagementService.saveProfile(request.getProfile());
			
			response.setResponseCode(Response.TX_SUCCESS);
			response.setProfileId(profile.getProfileId());
		} catch (Exception e) {
			logger.info("createProfile|REST|Unexpected Error " + e.getMessage());
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(Response.UNEXPECTED_ERROR);
			
		}
		
		logger.info("createProfile|REST|END|"
				+ (System.currentTimeMillis() - startTime)
				+ "|response=" + response);		
		return response;			
	}
	
	@RequestMapping(value = "api/isNICNumberExist/{nic}", method = RequestMethod.GET)
	public @ResponseBody
	IsNICNumberExistResponse isNICNumberExist(
			@PathVariable(value = "nic") String nic) {
		
		logger.info("isNICNumberExist|REST|START|"
				+ "NIC=" + nic);
		
		long startTime = System.currentTimeMillis();	
		
		String nICNumber = nic.trim();
		IsNICNumberExistResponse response =  new IsNICNumberExistResponse();
		
		try {
			Boolean isNICNumberExist = partnerManagementService.isNICNumberExist(nICNumber);
			
			response.setResponseCode(Response.TX_SUCCESS);
			response.setnICNumber(nICNumber);
			response.setNICNumberExist(isNICNumberExist.booleanValue());
			
		} catch (Exception e) {
			logger.info("isNICNumberExist|REST|Unexpected Error " + e.getMessage());
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(Response.UNEXPECTED_ERROR);			
		}
		
		logger.info("isNICNumberExist|REST|END|"
				+ (System.currentTimeMillis() - startTime)
				+ "|response=" + response);
		return response;		
	}
	
	@RequestMapping(value = "api/isDeviceNoExist/{msisdn}", method = RequestMethod.GET)
	public @ResponseBody
	IsDeviceNoExistResponse isDeviceNoExist(
			@PathVariable(value = "msisdn") String msisdn)  {
		
		logger.info("isDeviceNoExist|REST|START|"
				+ "MSISDN=" + msisdn);
		
		long startTime = System.currentTimeMillis();
				
		String deviceNo = msisdn.trim();
		IsDeviceNoExistResponse response =  new IsDeviceNoExistResponse();
		
		try {
			Boolean isDeviceNoExist = partnerManagementService.isDeviceNoExist(deviceNo);
			
			response.setResponseCode(Response.TX_SUCCESS);
			response.setDeviceNumber(deviceNo);
			response.setDeviceNoExist(isDeviceNoExist);
			
		} catch (Exception e) {
			logger.info("isDeviceNoExist|REST|Unexpected Error " + e.getMessage());
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(Response.UNEXPECTED_ERROR);			
		}
		
		logger.info("isDeviceNoExist|REST|END|"
				+ (System.currentTimeMillis() - startTime)
				+ "|response=" + response);
		return response;
	}
	
	@RequestMapping(value = "api//getProfilesCreatedByUser", method = RequestMethod.POST)
	public @ResponseBody GetProfilesCreatedByUserResponse getProfilesCreatedByUser(
			@RequestBody GetProfilesCreatedByUserRequest request) {
		logger.info("getProfilesCreatedByUser|REST|START|"
				+ "request=" + request);
		
		long startTime = System.currentTimeMillis();		
				
		GetProfilesCreatedByUserResponse response =  new GetProfilesCreatedByUserResponse();
		
		if (request.getCreatedUser() == null || request.getCreatedUser().isEmpty()) {
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(msgSource.getMessage("created.user.null.empty", null, currentLocale));
			return response;
		}
		
		if (request.getPageNo() == 0) {
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(msgSource.getMessage("page.no.null.empty", null, currentLocale));
			return response;
		}
		
		if (request.getPageSize() == 0) {
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(msgSource.getMessage("page.size.null.empty", null, currentLocale));
			return response;
		}
		
		try {
			List<PartnerProfileSummaryDTO> profSummaries = partnerManagementService.getProfilesCreatedByUser(
					request.getCreatedUser(), request.getProfId(), request.getSearchtStr(), 
					request.getPageNo(), request.getPageSize());
			
			response.setResponseCode(Response.TX_SUCCESS);
			response.addProfSummaries(profSummaries);
			
		} catch (Exception e) {
			logger.info("isDeviceNoExist|REST|Unexpected Error " + e.getMessage());
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(Response.UNEXPECTED_ERROR);			
		}
		
		logger.info("isDeviceNoExist|REST|END|"
				+ (System.currentTimeMillis() - startTime)
				+ "|response=" + response);
		return response;
	}
	
	@RequestMapping(value = "api/getProfilesCreatedByUserCount", method = RequestMethod.POST)
	public @ResponseBody GetProfilesCreatedByUserCountResponse getProfilesCreatedByUserCount(
			@RequestBody GetProfilesCreatedByUserRequest request) {
		logger.info("getProfilesCreatedByUserCount|REST|START|"
				+ "request=" + request);
		
		long startTime = System.currentTimeMillis();
				
		GetProfilesCreatedByUserCountResponse response =  new GetProfilesCreatedByUserCountResponse();
		
		if (request.getCreatedUser() == null || request.getCreatedUser().isEmpty()) {
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(msgSource.getMessage("created.user.null.empty", null, currentLocale));
			return response;
		}	
		
		try {
			Long recCount = partnerManagementService.getProfilesCreatedByUserCount(
					request.getCreatedUser(), request.getProfId(), request.getSearchtStr());
			
			response.setResponseCode(Response.TX_SUCCESS);
			response.setRecCount(recCount);
			
		} catch (Exception e) {
			logger.info("getProfilesCreatedByUserCount|REST|Unexpected Error " + e.getMessage());
			response.setResponseCode(Response.TX_ERROR);
			response.setResponseDescription(Response.UNEXPECTED_ERROR);			
		}
		
		logger.info("getProfilesCreatedByUserCount|REST|END|"
				+ (System.currentTimeMillis() - startTime)
				+ "|response=" + response);
		return response;
	}
	
	


}

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
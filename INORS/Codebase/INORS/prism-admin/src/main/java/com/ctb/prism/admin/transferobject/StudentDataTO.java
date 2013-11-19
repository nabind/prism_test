package com.ctb.prism.admin.transferobject;

import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

import com.ctb.prism.core.transferobject.BaseTO;

@Record(minOccurs = 1)
public class StudentDataTO extends BaseTO {
	private static final long serialVersionUID = -307431328130789728L;

	@Field(at = 0)
	private String custID = "";
	@Field(at = 1)
	private String rosterID = "";
	@Field(at = 2)
	private String hierarchyA_Name = "";
	@Field(at = 3)
	private String hierarchyA_Code = "";
	@Field(at = 4)
	private String hierarchyA_SpCodes = "";
	@Field(at = 5)
	private String hierarchyB_Name = "";
	@Field(at = 6)
	private String hierarchyB_Code = "";
	@Field(at = 7)
	private String hierarchyB_SpCodes = "";
	@Field(at = 8)
	private String hierarchyC_Name = "";
	@Field(at = 9)
	private String hierarchyC_Code = "";
	@Field(at = 10)
	private String hierarchyC_SpCodes = "";
	@Field(at = 11)
	private String hierarchyD_Name = "";
	@Field(at = 12)
	private String hierarchyD_Code = "";
	@Field(at = 13)
	private String hierarchyD_SpCodes = "";
	@Field(at = 14)
	private String hierarchyE_Name = "";
	@Field(at = 15)
	private String hierarchyE_Code = "";
	@Field(at = 16)
	private String hierarchyE_SpCodes = "";
	@Field(at = 17)
	private String hierarchyF_Name = "";
	@Field(at = 18)
	private String hierarchyF_Code = "";
	@Field(at = 19)
	private String hierarchyF_SpCodes = "";
	@Field(at = 20)
	private String hierarchyG_Name = "";
	@Field(at = 21)
	private String hierarchyG_Code = "";
	@Field(at = 22)
	private String hierarchyG_SpCodes = "";
	@Field(at = 23)
	private String lastName = "";
	@Field(at = 24)
	private String firstName = "";
	@Field(at = 25)
	private String middleInitial = "";
	@Field(at = 26)
	private String dateOfBirth = "";
	@Field(at = 27)
	private String form = "";
	@Field(at = 28)
	private String changedFormFlag = "";
	@Field(at = 29)
	private String testLang = "";
	@Field(at = 30)
	private String testMode = "";
	@Field(at = 31)
	private String gender = "";
	@Field(at = 32)
	private String resolvedEthnicity = "";
	@Field(at = 33)
	private String studentID = "";
	@Field(at = 34)
	private String phoneNumber = "";
	@Field(at = 35)
	private String lithocode = "";
	@Field(at = 36)
	private String imagingID = "";
	@Field(at = 37)
	private String streetAddress = "";
	@Field(at = 38)
	private String aptNo = "";
	@Field(at = 39)
	private String city = "";
	@Field(at = 40)
	private String state = "";
	@Field(at = 41)
	private String zipCode = "";
	@Field(at = 42)
	private String county_Code = "";
	@Field(at = 43)
	private String educ_Center_Code = "";
	@Field(at = 44)
	private String acc_Audio_Rd = "";
	@Field(at = 45)
	private String acc_Audio_Wr = "";
	@Field(at = 46)
	private String acc_Audio_Math1 = "";
	@Field(at = 47)
	private String acc_Audio_Math2 = "";
	@Field(at = 48)
	private String acc_Audio_Sc = "";
	@Field(at = 49)
	private String acc_Audio_SS = "";
	@Field(at = 50)
	private String acc_Breaks_Rd = "";
	@Field(at = 51)
	private String acc_Breaks_Wr = "";
	@Field(at = 52)
	private String acc_Breaks_Math1 = "";
	@Field(at = 53)
	private String acc_Breaks_Math2 = "";
	@Field(at = 54)
	private String acc_Breaks_SC = "";
	@Field(at = 55)
	private String acc_Breaks_SS = "";
	@Field(at = 56)
	private String acc_Calculator_Rd = "";
	@Field(at = 57)
	private String acc_Calculator_Wr = "";
	@Field(at = 58)
	private String acc_Calculator_Math1 = "";
	@Field(at = 59)
	private String acc_Calculator_SS = "";
	@Field(at = 60)
	private String acc_Duration1_25_Rd = "";
	@Field(at = 61)
	private String acc_Duration1_25_Wr = "";
	@Field(at = 62)
	private String acc_Duration1_25_Math1 = "";
	@Field(at = 63)
	private String acc_Duration1_25_Math2 = "";
	@Field(at = 64)
	private String acc_Duration1_25_Sc = "";
	@Field(at = 65)
	private String acc_Duration1_25_SS = "";
	@Field(at = 66)
	private String acc_Duration1_5_Rd = "";
	@Field(at = 67)
	private String acc_Duration1_5_Wr = "";
	@Field(at = 68)
	private String acc_Duration1_5_Math1 = "";
	@Field(at = 69)
	private String acc_Duration1_5_Math2 = "";
	@Field(at = 70)
	private String acc_Duration1_5_Sc = "";
	@Field(at = 71)
	private String acc_Duration1_5_SS = "";
	@Field(at = 72)
	private String acc_Duration2_0_Rd = "";
	@Field(at = 73)
	private String acc_Duration2_0_Wr = "";
	@Field(at = 74)
	private String acc_Duration2_0_Math1 = "";
	@Field(at = 75)
	private String acc_Duration2_0_Math2 = "";
	@Field(at = 76)
	private String acc_Duration2_0_Sc = "";
	@Field(at = 77)
	private String acc_Duration2_0_SS = "";
	@Field(at = 78)
	private String acc_PhysicalSupport_Rd = "";
	@Field(at = 79)
	private String acc_PhysicalSupport_Wr = "";
	@Field(at = 80)
	private String acc_PhysicalSupport_Math1 = "";
	@Field(at = 81)
	private String acc_PhysicalSupport_Math2 = "";
	@Field(at = 82)
	private String acc_PhysicalSupport_Sc = "";
	@Field(at = 83)
	private String acc_PhysicalSupport_SS = "";
	@Field(at = 84)
	private String acc_Scribe_Rd = "";
	@Field(at = 85)
	private String acc_Scribe_Wr = "";
	@Field(at = 86)
	private String acc_Scribe_Math1 = "";
	@Field(at = 87)
	private String acc_Scribe_Math2 = "";
	@Field(at = 88)
	private String acc_Scribe_Sc = "";
	@Field(at = 89)
	private String acc_Scribe_SS = "";
	@Field(at = 90)
	private String acc_TechDevice_Rd = "";
	@Field(at = 91)
	private String acc_TechDevice_Wr = "";
	@Field(at = 92)
	private String acc_TechDevice_Math1 = "";
	@Field(at = 93)
	private String acc_TechDevice_Math2 = "";
	@Field(at = 94)
	private String acc_TechDevice_Sc = "";
	@Field(at = 95)
	private String acc_TechDevice_SS = "";
	@Field(at = 96)
	private String acc_SepRoom_Rd = "";
	@Field(at = 97)
	private String acc_SepRoom_Wr = "";
	@Field(at = 98)
	private String acc_SepRoom_Math1 = "";
	@Field(at = 99)
	private String acc_SepRoom_Math2 = "";
	@Field(at = 100)
	private String acc_SepRoom_Sc = "";
	@Field(at = 101)
	private String acc_SepRoom_SS = "";
	@Field(at = 102)
	private String acc_SmGroup_Rd = "";
	@Field(at = 103)
	private String acc_SmGroup_Wr = "";
	@Field(at = 104)
	private String acc_SmGroup_Math1 = "";
	@Field(at = 105)
	private String acc_SmGroup_Math2 = "";
	@Field(at = 106)
	private String acc_SmGroup_Sc = "";
	@Field(at = 107)
	private String acc_SmGroup_SS = "";
	@Field(at = 108)
	private String acc_Other_Rd = "";
	@Field(at = 109)
	private String acc_Other_Wr = "";
	@Field(at = 110)
	private String acc_Other_Math1 = "";
	@Field(at = 111)
	private String acc_Other_Math2 = "";
	@Field(at = 112)
	private String acc_Other_Sc = "";
	@Field(at = 113)
	private String acc_Other_SS = "";
	@Field(at = 114)
	private String examiner_Ack_Accomm = "";
	@Field(at = 115)
	private String homeLang = "";
	@Field(at = 116)
	private String k_12_Educ_Completed = "";
	@Field(at = 117)
	private String subj_Taken_Passed_Rd = "";
	@Field(at = 118)
	private String subj_Taken_Passed_Wr = "";
	@Field(at = 119)
	private String subj_Taken_Passed_Math = "";
	@Field(at = 120)
	private String subj_Taken_Passed_Sc = "";
	@Field(at = 121)
	private String subj_Taken_Passed_SS = "";
	@Field(at = 122)
	private String subj_Taken_Passed_None = "";
	@Field(at = 123)
	private String no_Times_Taken_Rd = "";
	@Field(at = 124)
	private String no_Times_Taken_Wr = "";
	@Field(at = 125)
	private String no_Times_Taken_Math = "";
	@Field(at = 126)
	private String no_Times_Taken_Sc = "";
	@Field(at = 127)
	private String no_Times_Taken_SS = "";
	@Field(at = 128)
	private String retake_Rd = "";
	@Field(at = 129)
	private String retake_Wr = "";
	@Field(at = 130)
	private String retake_Math = "";
	@Field(at = 131)
	private String retake_Sc = "";
	@Field(at = 132)
	private String retake_SS = "";
	@Field(at = 133)
	private String retake_None = "";
	@Field(at = 134)
	private String take_Readiness_Assessment = "";
	@Field(at = 135)
	private String prepare_County_Prog = "";
	@Field(at = 136)
	private String prepare_Sch_Dist_Prog = "";
	@Field(at = 137)
	private String prepare_Military_Prog = "";
	@Field(at = 138)
	private String prepare_Religious_Prog = "";
	@Field(at = 139)
	private String prepare_Purchased_My_Own_Study_Books = "";
	@Field(at = 140)
	private String prepare_Online_Study_Prog = "";
	@Field(at = 141)
	private String prepare_Homeschool = "";
	@Field(at = 142)
	private String prepare_Tutor = "";
	@Field(at = 143)
	private String prepare_Self_Taught = "";
	@Field(at = 144)
	private String recent_Class_Rd = "";
	@Field(at = 145)
	private String recent_Class_Wr = "";
	@Field(at = 146)
	private String recent_Class_Math = "";
	@Field(at = 147)
	private String recent_Class_Sc = "";
	@Field(at = 148)
	private String recent_Class_SS = "";
	@Field(at = 149)
	private String months_Studied_Subj_Rd = "";
	@Field(at = 150)
	private String months_Studied_Subj_Wr = "";
	@Field(at = 151)
	private String months_Studied_Subj_Math = "";
	@Field(at = 152)
	private String months_Studied_Subj_Sc = "";
	@Field(at = 153)
	private String months_Studied_Subj_SS = "";
	@Field(at = 154)
	private String grade_in_Subj_Rd = "";
	@Field(at = 155)
	private String grade_in_Subj_Wr = "";
	@Field(at = 156)
	private String grade_in_Subj_Math = "";
	@Field(at = 157)
	private String grade_in_Subj_Sc = "";
	@Field(at = 158)
	private String grade_in_Subj_SS = "";
	@Field(at = 159)
	private String testFormat_Braille = "";
	@Field(at = 160)
	private String testFormat_LP = "";
	@Field(at = 161)
	private String local_Field_1 = "";
	@Field(at = 162)
	private String local_Field_2 = "";
	@Field(at = 163)
	private String local_Field_3 = "";
	@Field(at = 164)
	private String local_Field_4 = "";
	@Field(at = 165)
	private String local_Field_5 = "";
	@Field(at = 166)
	private String local_Field_6 = "";
	@Field(at = 167)
	private String local_Field_7 = "";
	@Field(at = 168)
	private String local_Field_8 = "";
	@Field(at = 169)
	private String local_Field_9 = "";
	@Field(at = 170)
	private String local_Field_10 = "";
	@Field(at = 171)
	private String local_Field_11 = "";
	@Field(at = 172)
	private String local_Field_12 = "";
	@Field(at = 173)
	private String local_Field_13 = "";
	@Field(at = 174)
	private String local_Field_14 = "";
	@Field(at = 175)
	private String local_Field_15 = "";
	@Field(at = 176)
	private String local_Field_16 = "";
	@Field(at = 177)
	private String local_Field_17 = "";
	@Field(at = 178)
	private String local_Field_18 = "";
	@Field(at = 179)
	private String local_Field_19 = "";
	@Field(at = 180)
	private String local_Field_20 = "";
	@Field(at = 181)
	private String candidate_Acknowledgement = "";
	// Reading , len
	@Field(at = 182)
	private String reading_dateTestTaken = "";
	@Field(at = 183)
	private String reading_numberCorrect = "";
	@Field(at = 184)
	private String reading_scaleScore = "";
	@Field(at = 185)
	private String reading_hSE_Score = "";
	@Field(at = 186)
	private String reading_percentileRank = "";
	@Field(at = 187)
	private String reading_nCE = "";
	@Field(at = 188)
	private String reading_std_Obj_Mstry_Scr_1 = "";
	@Field(at = 189)
	private String reading_not_All_items_atmpt_1 = "";
	@Field(at = 190)
	private String reading_std_Obj_Mstry_Scr_2 = "";
	@Field(at = 191)
	private String reading_not_All_items_atmpt_2 = "";
	@Field(at = 192)
	private String reading_std_Obj_Mstry_Scr_3 = "";
	@Field(at = 193)
	private String reading_not_All_items_atmpt_3 = "";
	@Field(at = 194)
	private String reading_std_Obj_Mstry_Scr_4 = "";
	@Field(at = 195)
	private String reading_not_All_items_atmpt_4 = "";
	@Field(at = 196)
	private String reading_std_Obj_Mstry_Scr_5 = "";
	@Field(at = 197)
	private String reading_not_All_items_atmpt_5 = "";
	@Field(at = 198)
	private String reading_std_Obj_Mstry_Scr_6 = "";
	@Field(at = 199)
	private String reading_not_All_items_atmpt_6 = "";
	@Field(at = 200)
	private String reading_std_Obj_Mstry_Scr_7 = "";
	@Field(at = 201)
	private String reading_not_All_items_atmpt_7 = "";
	@Field(at = 202)
	private String reading_std_Obj_Mstry_Scr_8 = "";
	@Field(at = 203)
	private String reading_not_All_items_atmpt_8 = "";
	// Writing , len
	@Field(at = 204)
	private String writing_dateTestTaken = "";
	@Field(at = 205)
	private String writing_numberCorrect = "";
	@Field(at = 206)
	private String writing_scaleScore = "";
	@Field(at = 207)
	private String writing_hSE_Score = "";
	@Field(at = 208)
	private String writing_percentileRank = "";
	@Field(at = 209)
	private String writing_nCE = "";
	@Field(at = 210)
	private String writing_std_Obj_Mstry_Scr_1 = "";
	@Field(at = 211)
	private String writing_not_All_items_atmpt_1 = "";
	@Field(at = 212)
	private String writing_std_Obj_Mstry_Scr_2 = "";
	@Field(at = 213)
	private String writing_not_All_items_atmpt_2 = "";
	@Field(at = 214)
	private String writing_std_Obj_Mstry_Scr_3 = "";
	@Field(at = 215)
	private String writing_not_All_items_atmpt_3 = "";
	@Field(at = 216)
	private String writing_std_Obj_Mstry_Scr_4 = "";
	@Field(at = 217)
	private String writing_not_All_items_atmpt_4 = "";
	// ELA Test , leh
	@Field(at = 218)
	private String ela_scaleScore = "";
	@Field(at = 219)
	private String ela_hSE_Score = "";
	@Field(at = 220)
	private String ela_percentileRank = "";
	@Field(at = 221)
	private String ela_nCE = "";
	// Mathemat , leh
	@Field(at = 222)
	private String math_dateTestTaken = "";
	@Field(at = 223)
	private String math_numberCorrect = "";
	@Field(at = 224)
	private String math_scaleScore = "";
	@Field(at = 225)
	private String math_hSE_Score = "";
	@Field(at = 226)
	private String math_percentileRank = "";
	@Field(at = 227)
	private String math_nCE = "";
	@Field(at = 228)
	private String math_std_Obj_Mstry_Scr_1 = "";
	@Field(at = 229)
	private String math_not_All_items_atmpt_1 = "";
	@Field(at = 230)
	private String math_std_Obj_Mstry_Scr_2 = "";
	@Field(at = 231)
	private String math_not_All_items_atmpt_2 = "";
	@Field(at = 232)
	private String math_std_Obj_Mstry_Scr_3 = "";
	@Field(at = 233)
	private String math_not_All_items_atmpt_3 = "";
	@Field(at = 234)
	private String math_std_Obj_Mstry_Scr_4 = "";
	@Field(at = 235)
	private String math_not_All_items_atmpt_4 = "";
	@Field(at = 236)
	private String math_std_Obj_Mstry_Scr_5 = "";
	@Field(at = 237)
	private String math_not_All_items_atmpt_5 = "";
	@Field(at = 238)
	private String math_std_Obj_Mstry_Scr_6 = "";
	@Field(at = 239)
	private String math_not_All_items_atmpt_6 = "";
	@Field(at = 240)
	private String math_std_Obj_Mstry_Scr_7 = "";
	@Field(at = 241)
	private String math_not_All_items_atmpt_7 = "";
	// Science , len
	@Field(at = 242)
	private String science_dateTestTaken = "";
	@Field(at = 243)
	private String science_numberCorrect = "";
	@Field(at = 244)
	private String science_scaleScore = "";
	@Field(at = 245)
	private String science_hSE_Score = "";
	@Field(at = 246)
	private String science_percentileRank = "";
	@Field(at = 247)
	private String science_nCE = "";
	@Field(at = 248)
	private String science_std_Obj_Mstry_Scr_1 = "";
	@Field(at = 249)
	private String science_not_All_items_atmpt_1 = "";
	@Field(at = 250)
	private String science_std_Obj_Mstry_Scr_2 = "";
	@Field(at = 251)
	private String science_not_All_items_atmpt_2 = "";
	@Field(at = 252)
	private String science_std_Obj_Mstry_Scr_3 = "";
	@Field(at = 253)
	private String science_not_All_items_atmpt_3 = "";
	@Field(at = 254)
	private String science_std_Obj_Mstry_Scr_4 = "";
	@Field(at = 255)
	private String science_not_All_items_atmpt_4 = "";
	@Field(at = 256)
	private String science_std_Obj_Mstry_Scr_5 = "";
	@Field(at = 257)
	private String science_not_All_items_atmpt_5 = "";
	// Social S , leh
	@Field(at = 258)
	private String social_dateTestTaken = "";
	@Field(at = 259)
	private String social_numberCorrect = "";
	@Field(at = 260)
	private String social_scaleScore = "";
	@Field(at = 261)
	private String social_hSE_Score = "";
	@Field(at = 262)
	private String social_percentileRank = "";
	@Field(at = 263)
	private String social_nCE = "";
	@Field(at = 264)
	private String social_std_Obj_Mstry_Scr_1 = "";
	@Field(at = 265)
	private String social_not_All_items_atmpt_1 = "";
	@Field(at = 266)
	private String social_std_Obj_Mstry_Scr_2 = "";
	@Field(at = 267)
	private String social_not_All_items_atmpt_2 = "";
	@Field(at = 268)
	private String social_std_Obj_Mstry_Scr_3 = "";
	@Field(at = 269)
	private String social_not_All_items_atmpt_3 = "";
	@Field(at = 270)
	private String social_std_Obj_Mstry_Scr_4 = "";
	@Field(at = 271)
	private String social_not_All_items_atmpt_4 = "";
	@Field(at = 272)
	private String social_std_Obj_Mstry_Scr_5 = "";
	@Field(at = 273)
	private String social_not_All_items_atmpt_5 = "";
	@Field(at = 274)
	private String social_std_Obj_Mstry_Scr_6 = "";
	@Field(at = 275)
	private String social_not_All_items_atmpt_6 = "";
	@Field(at = 276)
	private String social_std_Obj_Mstry_Scr_7 = "";
	@Field(at = 277)
	private String social_not_All_items_atmpt_7 = "";
	// Overall , len
	@Field(at = 278)
	private String overall_scaleScore = "";
	@Field(at = 279)
	private String overall_hSE_Score = "";
	@Field(at = 280)
	private String overall_percentileRank = "";
	@Field(at = 281)
	private String overall_nCE = "";
	// OAS Sett , leh
	@Field(at = 282)
	private String oasReading_screenReader = "";
	@Field(at = 283)
	private String oasReading_onlineCalc = "";
	@Field(at = 284)
	private String oasReading_testPause = "";
	@Field(at = 285)
	private String oasReading_highlighter = "";
	@Field(at = 286)
	private String oasReading_blockingRuler = "";
	@Field(at = 287)
	private String oasReading_magnifyingGlass = "";
	@Field(at = 288)
	private String oasReading_fontAndBkgndClr = "";
	@Field(at = 289)
	private String oasReading_largeFont = "";
	@Field(at = 290)
	private String oasReading_musicPlayer = "";
	@Field(at = 291)
	private String oasReading_extendedTime = "";
	@Field(at = 292)
	private String oasReading_maskingTool = "";
	// OAS Sett , leh
	@Field(at = 293)
	private String oasWriting_screenReader = "";
	@Field(at = 294)
	private String oasWriting_onlineCalc = "";
	@Field(at = 295)
	private String oasWriting_testPause = "";
	@Field(at = 296)
	private String oasWriting_highlighter = "";
	@Field(at = 297)
	private String oasWriting_blockingRuler = "";
	@Field(at = 298)
	private String oasWriting_magnifyingGlass = "";
	@Field(at = 299)
	private String oasWriting_fontAndBkgndClr = "";
	@Field(at = 300)
	private String oasWriting_largeFont = "";
	@Field(at = 301)
	private String oasWriting_musicPlayer = "";
	@Field(at = 302)
	private String oasWriting_extendedTime = "";
	@Field(at = 303)
	private String oasWriting_maskingTool = "";
	// OAS Sett , leh
	@Field(at = 304)
	private String oasMath_screenReader = "";
	@Field(at = 305)
	private String oasMath_onlineCalc = "";
	@Field(at = 306)
	private String oasMath_testPause = "";
	@Field(at = 307)
	private String oasMath_highlighter = "";
	@Field(at = 308)
	private String oasMath_blockingRuler = "";
	@Field(at = 309)
	private String oasMath_magnifyingGlass = "";
	@Field(at = 310)
	private String oasMath_fontAndBkgndClr = "";
	@Field(at = 311)
	private String oasMath_largeFont = "";
	@Field(at = 312)
	private String oasMath_musicPlayer = "";
	@Field(at = 313)
	private String oasMath_extendedTime = "";
	@Field(at = 314)
	private String oasMath_maskingTool = "";
	// OAS Sett , leh
	@Field(at = 315)
	private String oasScience_screenReader = "";
	@Field(at = 316)
	private String oasScience_onlineCalc = "";
	@Field(at = 317)
	private String oasScience_testPause = "";
	@Field(at = 318)
	private String oasScience_highlighter = "";
	@Field(at = 319)
	private String oasScience_blockingRuler = "";
	@Field(at = 320)
	private String oasScience_magnifyingGlass = "";
	@Field(at = 321)
	private String oasScience_fontAndBkgndClr = "";
	@Field(at = 322)
	private String oasScience_largeFont = "";
	@Field(at = 323)
	private String oasScience_musicPlayer = "";
	@Field(at = 324)
	private String oasScience_extendedTime = "";
	@Field(at = 325)
	private String oasScience_maskingTool = "";
	// OAS Sett , leh
	@Field(at = 326)
	private String oasSocial_screenReader = "";
	@Field(at = 327)
	private String oasSocial_onlineCalc = "";
	@Field(at = 328)
	private String oasSocial_testPause = "";
	@Field(at = 329)
	private String oasSocial_highlighter = "";
	@Field(at = 330)
	private String oasSocial_blockingRuler = "";
	@Field(at = 331)
	private String oasSocial_magnifyingGlass = "";
	@Field(at = 332)
	private String oasSocial_fontAndBkgndClr = "";
	@Field(at = 333)
	private String oasSocial_largeFont = "";
	@Field(at = 334)
	private String oasSocial_musicPlayer = "";
	@Field(at = 335)
	private String oasSocial_extendedTime = "";
	@Field(at = 336)
	private String oasSocial_maskingTool = "";
	// Item Res , leh
	@Field(at = 337)
	private String readingItems_SR = "";
	@Field(at = 338)
	private String readingItems_FU = "";
	@Field(at = 339)
	private String writingItems_SR = "";
	@Field(at = 340)
	private String writingItems_CR = "";
	@Field(at = 341)
	private String writingItems_FU = "";
	@Field(at = 342)
	private String mathItems_SR = "";
	@Field(at = 343)
	private String mathItems_GR_Status = "";
	@Field(at = 344)
	private String mathItems_GR_Edited = "";
	@Field(at = 345)
	private String mathItems_FU = "";
	@Field(at = 346)
	private String scienceItems_SR = "";
	@Field(at = 347)
	private String scienceItems_FU = "";
	@Field(at = 348)
	private String socialStudies_SR = "";
	@Field(at = 349)
	private String socialStudies_FU = "";
	@Field(at = 350)
	private String cTBUseField = "";

	public String getCustID() {
		return custID;
	}

	public void setCustID(String custID) {
		this.custID = custID;
	}

	public String getRosterID() {
		return rosterID;
	}

	public void setRosterID(String rosterID) {
		this.rosterID = rosterID;
	}

	public String getHierarchyA_Name() {
		return hierarchyA_Name;
	}

	public void setHierarchyA_Name(String hierarchyA_Name) {
		this.hierarchyA_Name = hierarchyA_Name;
	}

	public String getHierarchyA_Code() {
		return hierarchyA_Code;
	}

	public void setHierarchyA_Code(String hierarchyA_Code) {
		this.hierarchyA_Code = hierarchyA_Code;
	}

	public String getHierarchyA_SpCodes() {
		return hierarchyA_SpCodes;
	}

	public void setHierarchyA_SpCodes(String hierarchyA_SpCodes) {
		this.hierarchyA_SpCodes = hierarchyA_SpCodes;
	}

	public String getHierarchyB_Name() {
		return hierarchyB_Name;
	}

	public void setHierarchyB_Name(String hierarchyB_Name) {
		this.hierarchyB_Name = hierarchyB_Name;
	}

	public String getHierarchyB_Code() {
		return hierarchyB_Code;
	}

	public void setHierarchyB_Code(String hierarchyB_Code) {
		this.hierarchyB_Code = hierarchyB_Code;
	}

	public String getHierarchyB_SpCodes() {
		return hierarchyB_SpCodes;
	}

	public void setHierarchyB_SpCodes(String hierarchyB_SpCodes) {
		this.hierarchyB_SpCodes = hierarchyB_SpCodes;
	}

	public String getHierarchyC_Name() {
		return hierarchyC_Name;
	}

	public void setHierarchyC_Name(String hierarchyC_Name) {
		this.hierarchyC_Name = hierarchyC_Name;
	}

	public String getHierarchyC_Code() {
		return hierarchyC_Code;
	}

	public void setHierarchyC_Code(String hierarchyC_Code) {
		this.hierarchyC_Code = hierarchyC_Code;
	}

	public String getHierarchyC_SpCodes() {
		return hierarchyC_SpCodes;
	}

	public void setHierarchyC_SpCodes(String hierarchyC_SpCodes) {
		this.hierarchyC_SpCodes = hierarchyC_SpCodes;
	}

	public String getHierarchyD_Name() {
		return hierarchyD_Name;
	}

	public void setHierarchyD_Name(String hierarchyD_Name) {
		this.hierarchyD_Name = hierarchyD_Name;
	}

	public String getHierarchyD_Code() {
		return hierarchyD_Code;
	}

	public void setHierarchyD_Code(String hierarchyD_Code) {
		this.hierarchyD_Code = hierarchyD_Code;
	}

	public String getHierarchyD_SpCodes() {
		return hierarchyD_SpCodes;
	}

	public void setHierarchyD_SpCodes(String hierarchyD_SpCodes) {
		this.hierarchyD_SpCodes = hierarchyD_SpCodes;
	}

	public String getHierarchyE_Name() {
		return hierarchyE_Name;
	}

	public void setHierarchyE_Name(String hierarchyE_Name) {
		this.hierarchyE_Name = hierarchyE_Name;
	}

	public String getHierarchyE_Code() {
		return hierarchyE_Code;
	}

	public void setHierarchyE_Code(String hierarchyE_Code) {
		this.hierarchyE_Code = hierarchyE_Code;
	}

	public String getHierarchyE_SpCodes() {
		return hierarchyE_SpCodes;
	}

	public void setHierarchyE_SpCodes(String hierarchyE_SpCodes) {
		this.hierarchyE_SpCodes = hierarchyE_SpCodes;
	}

	public String getHierarchyF_Name() {
		return hierarchyF_Name;
	}

	public void setHierarchyF_Name(String hierarchyF_Name) {
		this.hierarchyF_Name = hierarchyF_Name;
	}

	public String getHierarchyF_Code() {
		return hierarchyF_Code;
	}

	public void setHierarchyF_Code(String hierarchyF_Code) {
		this.hierarchyF_Code = hierarchyF_Code;
	}

	public String getHierarchyF_SpCodes() {
		return hierarchyF_SpCodes;
	}

	public void setHierarchyF_SpCodes(String hierarchyF_SpCodes) {
		this.hierarchyF_SpCodes = hierarchyF_SpCodes;
	}

	public String getHierarchyG_Name() {
		return hierarchyG_Name;
	}

	public void setHierarchyG_Name(String hierarchyG_Name) {
		this.hierarchyG_Name = hierarchyG_Name;
	}

	public String getHierarchyG_Code() {
		return hierarchyG_Code;
	}

	public void setHierarchyG_Code(String hierarchyG_Code) {
		this.hierarchyG_Code = hierarchyG_Code;
	}

	public String getHierarchyG_SpCodes() {
		return hierarchyG_SpCodes;
	}

	public void setHierarchyG_SpCodes(String hierarchyG_SpCodes) {
		this.hierarchyG_SpCodes = hierarchyG_SpCodes;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getChangedFormFlag() {
		return changedFormFlag;
	}

	public void setChangedFormFlag(String changedFormFlag) {
		this.changedFormFlag = changedFormFlag;
	}

	public String getTestLang() {
		return testLang;
	}

	public void setTestLang(String testLang) {
		this.testLang = testLang;
	}

	public String getTestMode() {
		return testMode;
	}

	public void setTestMode(String testMode) {
		this.testMode = testMode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getResolvedEthnicity() {
		return resolvedEthnicity;
	}

	public void setResolvedEthnicity(String resolvedEthnicity) {
		this.resolvedEthnicity = resolvedEthnicity;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLithocode() {
		return lithocode;
	}

	public void setLithocode(String lithocode) {
		this.lithocode = lithocode;
	}

	public String getImagingID() {
		return imagingID;
	}

	public void setImagingID(String imagingID) {
		this.imagingID = imagingID;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getAptNo() {
		return aptNo;
	}

	public void setAptNo(String aptNo) {
		this.aptNo = aptNo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCounty_Code() {
		return county_Code;
	}

	public void setCounty_Code(String county_Code) {
		this.county_Code = county_Code;
	}

	public String getEduc_Center_Code() {
		return educ_Center_Code;
	}

	public void setEduc_Center_Code(String educ_Center_Code) {
		this.educ_Center_Code = educ_Center_Code;
	}

	public String getAcc_Audio_Rd() {
		return acc_Audio_Rd;
	}

	public void setAcc_Audio_Rd(String acc_Audio_Rd) {
		this.acc_Audio_Rd = acc_Audio_Rd;
	}

	public String getAcc_Audio_Wr() {
		return acc_Audio_Wr;
	}

	public void setAcc_Audio_Wr(String acc_Audio_Wr) {
		this.acc_Audio_Wr = acc_Audio_Wr;
	}

	public String getAcc_Audio_Math1() {
		return acc_Audio_Math1;
	}

	public void setAcc_Audio_Math1(String acc_Audio_Math1) {
		this.acc_Audio_Math1 = acc_Audio_Math1;
	}

	public String getAcc_Audio_Math2() {
		return acc_Audio_Math2;
	}

	public void setAcc_Audio_Math2(String acc_Audio_Math2) {
		this.acc_Audio_Math2 = acc_Audio_Math2;
	}

	public String getAcc_Audio_Sc() {
		return acc_Audio_Sc;
	}

	public void setAcc_Audio_Sc(String acc_Audio_Sc) {
		this.acc_Audio_Sc = acc_Audio_Sc;
	}

	public String getAcc_Audio_SS() {
		return acc_Audio_SS;
	}

	public void setAcc_Audio_SS(String acc_Audio_SS) {
		this.acc_Audio_SS = acc_Audio_SS;
	}

	public String getAcc_Breaks_Rd() {
		return acc_Breaks_Rd;
	}

	public void setAcc_Breaks_Rd(String acc_Breaks_Rd) {
		this.acc_Breaks_Rd = acc_Breaks_Rd;
	}

	public String getAcc_Breaks_Wr() {
		return acc_Breaks_Wr;
	}

	public void setAcc_Breaks_Wr(String acc_Breaks_Wr) {
		this.acc_Breaks_Wr = acc_Breaks_Wr;
	}

	public String getAcc_Breaks_Math1() {
		return acc_Breaks_Math1;
	}

	public void setAcc_Breaks_Math1(String acc_Breaks_Math1) {
		this.acc_Breaks_Math1 = acc_Breaks_Math1;
	}

	public String getAcc_Breaks_Math2() {
		return acc_Breaks_Math2;
	}

	public void setAcc_Breaks_Math2(String acc_Breaks_Math2) {
		this.acc_Breaks_Math2 = acc_Breaks_Math2;
	}

	public String getAcc_Breaks_SC() {
		return acc_Breaks_SC;
	}

	public void setAcc_Breaks_SC(String acc_Breaks_SC) {
		this.acc_Breaks_SC = acc_Breaks_SC;
	}

	public String getAcc_Breaks_SS() {
		return acc_Breaks_SS;
	}

	public void setAcc_Breaks_SS(String acc_Breaks_SS) {
		this.acc_Breaks_SS = acc_Breaks_SS;
	}

	public String getAcc_Calculator_Rd() {
		return acc_Calculator_Rd;
	}

	public void setAcc_Calculator_Rd(String acc_Calculator_Rd) {
		this.acc_Calculator_Rd = acc_Calculator_Rd;
	}

	public String getAcc_Calculator_Wr() {
		return acc_Calculator_Wr;
	}

	public void setAcc_Calculator_Wr(String acc_Calculator_Wr) {
		this.acc_Calculator_Wr = acc_Calculator_Wr;
	}

	public String getAcc_Calculator_Math1() {
		return acc_Calculator_Math1;
	}

	public void setAcc_Calculator_Math1(String acc_Calculator_Math1) {
		this.acc_Calculator_Math1 = acc_Calculator_Math1;
	}

	public String getAcc_Calculator_SS() {
		return acc_Calculator_SS;
	}

	public void setAcc_Calculator_SS(String acc_Calculator_SS) {
		this.acc_Calculator_SS = acc_Calculator_SS;
	}

	public String getAcc_Duration1_25_Rd() {
		return acc_Duration1_25_Rd;
	}

	public void setAcc_Duration1_25_Rd(String acc_Duration1_25_Rd) {
		this.acc_Duration1_25_Rd = acc_Duration1_25_Rd;
	}

	public String getAcc_Duration1_25_Wr() {
		return acc_Duration1_25_Wr;
	}

	public void setAcc_Duration1_25_Wr(String acc_Duration1_25_Wr) {
		this.acc_Duration1_25_Wr = acc_Duration1_25_Wr;
	}

	public String getAcc_Duration1_25_Math1() {
		return acc_Duration1_25_Math1;
	}

	public void setAcc_Duration1_25_Math1(String acc_Duration1_25_Math1) {
		this.acc_Duration1_25_Math1 = acc_Duration1_25_Math1;
	}

	public String getAcc_Duration1_25_Math2() {
		return acc_Duration1_25_Math2;
	}

	public void setAcc_Duration1_25_Math2(String acc_Duration1_25_Math2) {
		this.acc_Duration1_25_Math2 = acc_Duration1_25_Math2;
	}

	public String getAcc_Duration1_25_Sc() {
		return acc_Duration1_25_Sc;
	}

	public void setAcc_Duration1_25_Sc(String acc_Duration1_25_Sc) {
		this.acc_Duration1_25_Sc = acc_Duration1_25_Sc;
	}

	public String getAcc_Duration1_25_SS() {
		return acc_Duration1_25_SS;
	}

	public void setAcc_Duration1_25_SS(String acc_Duration1_25_SS) {
		this.acc_Duration1_25_SS = acc_Duration1_25_SS;
	}

	public String getAcc_Duration1_5_Rd() {
		return acc_Duration1_5_Rd;
	}

	public void setAcc_Duration1_5_Rd(String acc_Duration1_5_Rd) {
		this.acc_Duration1_5_Rd = acc_Duration1_5_Rd;
	}

	public String getAcc_Duration1_5_Wr() {
		return acc_Duration1_5_Wr;
	}

	public void setAcc_Duration1_5_Wr(String acc_Duration1_5_Wr) {
		this.acc_Duration1_5_Wr = acc_Duration1_5_Wr;
	}

	public String getAcc_Duration1_5_Math1() {
		return acc_Duration1_5_Math1;
	}

	public void setAcc_Duration1_5_Math1(String acc_Duration1_5_Math1) {
		this.acc_Duration1_5_Math1 = acc_Duration1_5_Math1;
	}

	public String getAcc_Duration1_5_Math2() {
		return acc_Duration1_5_Math2;
	}

	public void setAcc_Duration1_5_Math2(String acc_Duration1_5_Math2) {
		this.acc_Duration1_5_Math2 = acc_Duration1_5_Math2;
	}

	public String getAcc_Duration1_5_Sc() {
		return acc_Duration1_5_Sc;
	}

	public void setAcc_Duration1_5_Sc(String acc_Duration1_5_Sc) {
		this.acc_Duration1_5_Sc = acc_Duration1_5_Sc;
	}

	public String getAcc_Duration1_5_SS() {
		return acc_Duration1_5_SS;
	}

	public void setAcc_Duration1_5_SS(String acc_Duration1_5_SS) {
		this.acc_Duration1_5_SS = acc_Duration1_5_SS;
	}

	public String getAcc_Duration2_0_Rd() {
		return acc_Duration2_0_Rd;
	}

	public void setAcc_Duration2_0_Rd(String acc_Duration2_0_Rd) {
		this.acc_Duration2_0_Rd = acc_Duration2_0_Rd;
	}

	public String getAcc_Duration2_0_Wr() {
		return acc_Duration2_0_Wr;
	}

	public void setAcc_Duration2_0_Wr(String acc_Duration2_0_Wr) {
		this.acc_Duration2_0_Wr = acc_Duration2_0_Wr;
	}

	public String getAcc_Duration2_0_Math1() {
		return acc_Duration2_0_Math1;
	}

	public void setAcc_Duration2_0_Math1(String acc_Duration2_0_Math1) {
		this.acc_Duration2_0_Math1 = acc_Duration2_0_Math1;
	}

	public String getAcc_Duration2_0_Math2() {
		return acc_Duration2_0_Math2;
	}

	public void setAcc_Duration2_0_Math2(String acc_Duration2_0_Math2) {
		this.acc_Duration2_0_Math2 = acc_Duration2_0_Math2;
	}

	public String getAcc_Duration2_0_Sc() {
		return acc_Duration2_0_Sc;
	}

	public void setAcc_Duration2_0_Sc(String acc_Duration2_0_Sc) {
		this.acc_Duration2_0_Sc = acc_Duration2_0_Sc;
	}

	public String getAcc_Duration2_0_SS() {
		return acc_Duration2_0_SS;
	}

	public void setAcc_Duration2_0_SS(String acc_Duration2_0_SS) {
		this.acc_Duration2_0_SS = acc_Duration2_0_SS;
	}

	public String getAcc_PhysicalSupport_Rd() {
		return acc_PhysicalSupport_Rd;
	}

	public void setAcc_PhysicalSupport_Rd(String acc_PhysicalSupport_Rd) {
		this.acc_PhysicalSupport_Rd = acc_PhysicalSupport_Rd;
	}

	public String getAcc_PhysicalSupport_Wr() {
		return acc_PhysicalSupport_Wr;
	}

	public void setAcc_PhysicalSupport_Wr(String acc_PhysicalSupport_Wr) {
		this.acc_PhysicalSupport_Wr = acc_PhysicalSupport_Wr;
	}

	public String getAcc_PhysicalSupport_Math1() {
		return acc_PhysicalSupport_Math1;
	}

	public void setAcc_PhysicalSupport_Math1(String acc_PhysicalSupport_Math1) {
		this.acc_PhysicalSupport_Math1 = acc_PhysicalSupport_Math1;
	}

	public String getAcc_PhysicalSupport_Math2() {
		return acc_PhysicalSupport_Math2;
	}

	public void setAcc_PhysicalSupport_Math2(String acc_PhysicalSupport_Math2) {
		this.acc_PhysicalSupport_Math2 = acc_PhysicalSupport_Math2;
	}

	public String getAcc_PhysicalSupport_Sc() {
		return acc_PhysicalSupport_Sc;
	}

	public void setAcc_PhysicalSupport_Sc(String acc_PhysicalSupport_Sc) {
		this.acc_PhysicalSupport_Sc = acc_PhysicalSupport_Sc;
	}

	public String getAcc_PhysicalSupport_SS() {
		return acc_PhysicalSupport_SS;
	}

	public void setAcc_PhysicalSupport_SS(String acc_PhysicalSupport_SS) {
		this.acc_PhysicalSupport_SS = acc_PhysicalSupport_SS;
	}

	public String getAcc_Scribe_Rd() {
		return acc_Scribe_Rd;
	}

	public void setAcc_Scribe_Rd(String acc_Scribe_Rd) {
		this.acc_Scribe_Rd = acc_Scribe_Rd;
	}

	public String getAcc_Scribe_Wr() {
		return acc_Scribe_Wr;
	}

	public void setAcc_Scribe_Wr(String acc_Scribe_Wr) {
		this.acc_Scribe_Wr = acc_Scribe_Wr;
	}

	public String getAcc_Scribe_Math1() {
		return acc_Scribe_Math1;
	}

	public void setAcc_Scribe_Math1(String acc_Scribe_Math1) {
		this.acc_Scribe_Math1 = acc_Scribe_Math1;
	}

	public String getAcc_Scribe_Math2() {
		return acc_Scribe_Math2;
	}

	public void setAcc_Scribe_Math2(String acc_Scribe_Math2) {
		this.acc_Scribe_Math2 = acc_Scribe_Math2;
	}

	public String getAcc_Scribe_Sc() {
		return acc_Scribe_Sc;
	}

	public void setAcc_Scribe_Sc(String acc_Scribe_Sc) {
		this.acc_Scribe_Sc = acc_Scribe_Sc;
	}

	public String getAcc_Scribe_SS() {
		return acc_Scribe_SS;
	}

	public void setAcc_Scribe_SS(String acc_Scribe_SS) {
		this.acc_Scribe_SS = acc_Scribe_SS;
	}

	public String getAcc_TechDevice_Rd() {
		return acc_TechDevice_Rd;
	}

	public void setAcc_TechDevice_Rd(String acc_TechDevice_Rd) {
		this.acc_TechDevice_Rd = acc_TechDevice_Rd;
	}

	public String getAcc_TechDevice_Wr() {
		return acc_TechDevice_Wr;
	}

	public void setAcc_TechDevice_Wr(String acc_TechDevice_Wr) {
		this.acc_TechDevice_Wr = acc_TechDevice_Wr;
	}

	public String getAcc_TechDevice_Math1() {
		return acc_TechDevice_Math1;
	}

	public void setAcc_TechDevice_Math1(String acc_TechDevice_Math1) {
		this.acc_TechDevice_Math1 = acc_TechDevice_Math1;
	}

	public String getAcc_TechDevice_Math2() {
		return acc_TechDevice_Math2;
	}

	public void setAcc_TechDevice_Math2(String acc_TechDevice_Math2) {
		this.acc_TechDevice_Math2 = acc_TechDevice_Math2;
	}

	public String getAcc_TechDevice_Sc() {
		return acc_TechDevice_Sc;
	}

	public void setAcc_TechDevice_Sc(String acc_TechDevice_Sc) {
		this.acc_TechDevice_Sc = acc_TechDevice_Sc;
	}

	public String getAcc_TechDevice_SS() {
		return acc_TechDevice_SS;
	}

	public void setAcc_TechDevice_SS(String acc_TechDevice_SS) {
		this.acc_TechDevice_SS = acc_TechDevice_SS;
	}

	public String getAcc_SepRoom_Rd() {
		return acc_SepRoom_Rd;
	}

	public void setAcc_SepRoom_Rd(String acc_SepRoom_Rd) {
		this.acc_SepRoom_Rd = acc_SepRoom_Rd;
	}

	public String getAcc_SepRoom_Wr() {
		return acc_SepRoom_Wr;
	}

	public void setAcc_SepRoom_Wr(String acc_SepRoom_Wr) {
		this.acc_SepRoom_Wr = acc_SepRoom_Wr;
	}

	public String getAcc_SepRoom_Math1() {
		return acc_SepRoom_Math1;
	}

	public void setAcc_SepRoom_Math1(String acc_SepRoom_Math1) {
		this.acc_SepRoom_Math1 = acc_SepRoom_Math1;
	}

	public String getAcc_SepRoom_Math2() {
		return acc_SepRoom_Math2;
	}

	public void setAcc_SepRoom_Math2(String acc_SepRoom_Math2) {
		this.acc_SepRoom_Math2 = acc_SepRoom_Math2;
	}

	public String getAcc_SepRoom_Sc() {
		return acc_SepRoom_Sc;
	}

	public void setAcc_SepRoom_Sc(String acc_SepRoom_Sc) {
		this.acc_SepRoom_Sc = acc_SepRoom_Sc;
	}

	public String getAcc_SepRoom_SS() {
		return acc_SepRoom_SS;
	}

	public void setAcc_SepRoom_SS(String acc_SepRoom_SS) {
		this.acc_SepRoom_SS = acc_SepRoom_SS;
	}

	public String getAcc_SmGroup_Rd() {
		return acc_SmGroup_Rd;
	}

	public void setAcc_SmGroup_Rd(String acc_SmGroup_Rd) {
		this.acc_SmGroup_Rd = acc_SmGroup_Rd;
	}

	public String getAcc_SmGroup_Wr() {
		return acc_SmGroup_Wr;
	}

	public void setAcc_SmGroup_Wr(String acc_SmGroup_Wr) {
		this.acc_SmGroup_Wr = acc_SmGroup_Wr;
	}

	public String getAcc_SmGroup_Math1() {
		return acc_SmGroup_Math1;
	}

	public void setAcc_SmGroup_Math1(String acc_SmGroup_Math1) {
		this.acc_SmGroup_Math1 = acc_SmGroup_Math1;
	}

	public String getAcc_SmGroup_Math2() {
		return acc_SmGroup_Math2;
	}

	public void setAcc_SmGroup_Math2(String acc_SmGroup_Math2) {
		this.acc_SmGroup_Math2 = acc_SmGroup_Math2;
	}

	public String getAcc_SmGroup_Sc() {
		return acc_SmGroup_Sc;
	}

	public void setAcc_SmGroup_Sc(String acc_SmGroup_Sc) {
		this.acc_SmGroup_Sc = acc_SmGroup_Sc;
	}

	public String getAcc_SmGroup_SS() {
		return acc_SmGroup_SS;
	}

	public void setAcc_SmGroup_SS(String acc_SmGroup_SS) {
		this.acc_SmGroup_SS = acc_SmGroup_SS;
	}

	public String getAcc_Other_Rd() {
		return acc_Other_Rd;
	}

	public void setAcc_Other_Rd(String acc_Other_Rd) {
		this.acc_Other_Rd = acc_Other_Rd;
	}

	public String getAcc_Other_Wr() {
		return acc_Other_Wr;
	}

	public void setAcc_Other_Wr(String acc_Other_Wr) {
		this.acc_Other_Wr = acc_Other_Wr;
	}

	public String getAcc_Other_Math1() {
		return acc_Other_Math1;
	}

	public void setAcc_Other_Math1(String acc_Other_Math1) {
		this.acc_Other_Math1 = acc_Other_Math1;
	}

	public String getAcc_Other_Math2() {
		return acc_Other_Math2;
	}

	public void setAcc_Other_Math2(String acc_Other_Math2) {
		this.acc_Other_Math2 = acc_Other_Math2;
	}

	public String getAcc_Other_Sc() {
		return acc_Other_Sc;
	}

	public void setAcc_Other_Sc(String acc_Other_Sc) {
		this.acc_Other_Sc = acc_Other_Sc;
	}

	public String getAcc_Other_SS() {
		return acc_Other_SS;
	}

	public void setAcc_Other_SS(String acc_Other_SS) {
		this.acc_Other_SS = acc_Other_SS;
	}

	public String getExaminer_Ack_Accomm() {
		return examiner_Ack_Accomm;
	}

	public void setExaminer_Ack_Accomm(String examiner_Ack_Accomm) {
		this.examiner_Ack_Accomm = examiner_Ack_Accomm;
	}

	public String getHomeLang() {
		return homeLang;
	}

	public void setHomeLang(String homeLang) {
		this.homeLang = homeLang;
	}

	public String getK_12_Educ_Completed() {
		return k_12_Educ_Completed;
	}

	public void setK_12_Educ_Completed(String k_12_Educ_Completed) {
		this.k_12_Educ_Completed = k_12_Educ_Completed;
	}

	public String getSubj_Taken_Passed_Rd() {
		return subj_Taken_Passed_Rd;
	}

	public void setSubj_Taken_Passed_Rd(String subj_Taken_Passed_Rd) {
		this.subj_Taken_Passed_Rd = subj_Taken_Passed_Rd;
	}

	public String getSubj_Taken_Passed_Wr() {
		return subj_Taken_Passed_Wr;
	}

	public void setSubj_Taken_Passed_Wr(String subj_Taken_Passed_Wr) {
		this.subj_Taken_Passed_Wr = subj_Taken_Passed_Wr;
	}

	public String getSubj_Taken_Passed_Math() {
		return subj_Taken_Passed_Math;
	}

	public void setSubj_Taken_Passed_Math(String subj_Taken_Passed_Math) {
		this.subj_Taken_Passed_Math = subj_Taken_Passed_Math;
	}

	public String getSubj_Taken_Passed_Sc() {
		return subj_Taken_Passed_Sc;
	}

	public void setSubj_Taken_Passed_Sc(String subj_Taken_Passed_Sc) {
		this.subj_Taken_Passed_Sc = subj_Taken_Passed_Sc;
	}

	public String getSubj_Taken_Passed_SS() {
		return subj_Taken_Passed_SS;
	}

	public void setSubj_Taken_Passed_SS(String subj_Taken_Passed_SS) {
		this.subj_Taken_Passed_SS = subj_Taken_Passed_SS;
	}

	public String getSubj_Taken_Passed_None() {
		return subj_Taken_Passed_None;
	}

	public void setSubj_Taken_Passed_None(String subj_Taken_Passed_None) {
		this.subj_Taken_Passed_None = subj_Taken_Passed_None;
	}

	public String getNo_Times_Taken_Rd() {
		return no_Times_Taken_Rd;
	}

	public void setNo_Times_Taken_Rd(String no_Times_Taken_Rd) {
		this.no_Times_Taken_Rd = no_Times_Taken_Rd;
	}

	public String getNo_Times_Taken_Wr() {
		return no_Times_Taken_Wr;
	}

	public void setNo_Times_Taken_Wr(String no_Times_Taken_Wr) {
		this.no_Times_Taken_Wr = no_Times_Taken_Wr;
	}

	public String getNo_Times_Taken_Math() {
		return no_Times_Taken_Math;
	}

	public void setNo_Times_Taken_Math(String no_Times_Taken_Math) {
		this.no_Times_Taken_Math = no_Times_Taken_Math;
	}

	public String getNo_Times_Taken_Sc() {
		return no_Times_Taken_Sc;
	}

	public void setNo_Times_Taken_Sc(String no_Times_Taken_Sc) {
		this.no_Times_Taken_Sc = no_Times_Taken_Sc;
	}

	public String getNo_Times_Taken_SS() {
		return no_Times_Taken_SS;
	}

	public void setNo_Times_Taken_SS(String no_Times_Taken_SS) {
		this.no_Times_Taken_SS = no_Times_Taken_SS;
	}

	public String getRetake_Rd() {
		return retake_Rd;
	}

	public void setRetake_Rd(String retake_Rd) {
		this.retake_Rd = retake_Rd;
	}

	public String getRetake_Wr() {
		return retake_Wr;
	}

	public void setRetake_Wr(String retake_Wr) {
		this.retake_Wr = retake_Wr;
	}

	public String getRetake_Math() {
		return retake_Math;
	}

	public void setRetake_Math(String retake_Math) {
		this.retake_Math = retake_Math;
	}

	public String getRetake_Sc() {
		return retake_Sc;
	}

	public void setRetake_Sc(String retake_Sc) {
		this.retake_Sc = retake_Sc;
	}

	public String getRetake_SS() {
		return retake_SS;
	}

	public void setRetake_SS(String retake_SS) {
		this.retake_SS = retake_SS;
	}

	public String getRetake_None() {
		return retake_None;
	}

	public void setRetake_None(String retake_None) {
		this.retake_None = retake_None;
	}

	public String getTake_Readiness_Assessment() {
		return take_Readiness_Assessment;
	}

	public void setTake_Readiness_Assessment(String take_Readiness_Assessment) {
		this.take_Readiness_Assessment = take_Readiness_Assessment;
	}

	public String getPrepare_County_Prog() {
		return prepare_County_Prog;
	}

	public void setPrepare_County_Prog(String prepare_County_Prog) {
		this.prepare_County_Prog = prepare_County_Prog;
	}

	public String getPrepare_Sch_Dist_Prog() {
		return prepare_Sch_Dist_Prog;
	}

	public void setPrepare_Sch_Dist_Prog(String prepare_Sch_Dist_Prog) {
		this.prepare_Sch_Dist_Prog = prepare_Sch_Dist_Prog;
	}

	public String getPrepare_Military_Prog() {
		return prepare_Military_Prog;
	}

	public void setPrepare_Military_Prog(String prepare_Military_Prog) {
		this.prepare_Military_Prog = prepare_Military_Prog;
	}

	public String getPrepare_Religious_Prog() {
		return prepare_Religious_Prog;
	}

	public void setPrepare_Religious_Prog(String prepare_Religious_Prog) {
		this.prepare_Religious_Prog = prepare_Religious_Prog;
	}

	public String getPrepare_Purchased_My_Own_Study_Books() {
		return prepare_Purchased_My_Own_Study_Books;
	}

	public void setPrepare_Purchased_My_Own_Study_Books(
			String prepare_Purchased_My_Own_Study_Books) {
		this.prepare_Purchased_My_Own_Study_Books = prepare_Purchased_My_Own_Study_Books;
	}

	public String getPrepare_Online_Study_Prog() {
		return prepare_Online_Study_Prog;
	}

	public void setPrepare_Online_Study_Prog(String prepare_Online_Study_Prog) {
		this.prepare_Online_Study_Prog = prepare_Online_Study_Prog;
	}

	public String getPrepare_Homeschool() {
		return prepare_Homeschool;
	}

	public void setPrepare_Homeschool(String prepare_Homeschool) {
		this.prepare_Homeschool = prepare_Homeschool;
	}

	public String getPrepare_Tutor() {
		return prepare_Tutor;
	}

	public void setPrepare_Tutor(String prepare_Tutor) {
		this.prepare_Tutor = prepare_Tutor;
	}

	public String getPrepare_Self_Taught() {
		return prepare_Self_Taught;
	}

	public void setPrepare_Self_Taught(String prepare_Self_Taught) {
		this.prepare_Self_Taught = prepare_Self_Taught;
	}

	public String getRecent_Class_Rd() {
		return recent_Class_Rd;
	}

	public void setRecent_Class_Rd(String recent_Class_Rd) {
		this.recent_Class_Rd = recent_Class_Rd;
	}

	public String getRecent_Class_Wr() {
		return recent_Class_Wr;
	}

	public void setRecent_Class_Wr(String recent_Class_Wr) {
		this.recent_Class_Wr = recent_Class_Wr;
	}

	public String getRecent_Class_Math() {
		return recent_Class_Math;
	}

	public void setRecent_Class_Math(String recent_Class_Math) {
		this.recent_Class_Math = recent_Class_Math;
	}

	public String getRecent_Class_Sc() {
		return recent_Class_Sc;
	}

	public void setRecent_Class_Sc(String recent_Class_Sc) {
		this.recent_Class_Sc = recent_Class_Sc;
	}

	public String getRecent_Class_SS() {
		return recent_Class_SS;
	}

	public void setRecent_Class_SS(String recent_Class_SS) {
		this.recent_Class_SS = recent_Class_SS;
	}

	public String getMonths_Studied_Subj_Rd() {
		return months_Studied_Subj_Rd;
	}

	public void setMonths_Studied_Subj_Rd(String months_Studied_Subj_Rd) {
		this.months_Studied_Subj_Rd = months_Studied_Subj_Rd;
	}

	public String getMonths_Studied_Subj_Wr() {
		return months_Studied_Subj_Wr;
	}

	public void setMonths_Studied_Subj_Wr(String months_Studied_Subj_Wr) {
		this.months_Studied_Subj_Wr = months_Studied_Subj_Wr;
	}

	public String getMonths_Studied_Subj_Math() {
		return months_Studied_Subj_Math;
	}

	public void setMonths_Studied_Subj_Math(String months_Studied_Subj_Math) {
		this.months_Studied_Subj_Math = months_Studied_Subj_Math;
	}

	public String getMonths_Studied_Subj_Sc() {
		return months_Studied_Subj_Sc;
	}

	public void setMonths_Studied_Subj_Sc(String months_Studied_Subj_Sc) {
		this.months_Studied_Subj_Sc = months_Studied_Subj_Sc;
	}

	public String getMonths_Studied_Subj_SS() {
		return months_Studied_Subj_SS;
	}

	public void setMonths_Studied_Subj_SS(String months_Studied_Subj_SS) {
		this.months_Studied_Subj_SS = months_Studied_Subj_SS;
	}

	public String getGrade_in_Subj_Rd() {
		return grade_in_Subj_Rd;
	}

	public void setGrade_in_Subj_Rd(String grade_in_Subj_Rd) {
		this.grade_in_Subj_Rd = grade_in_Subj_Rd;
	}

	public String getGrade_in_Subj_Wr() {
		return grade_in_Subj_Wr;
	}

	public void setGrade_in_Subj_Wr(String grade_in_Subj_Wr) {
		this.grade_in_Subj_Wr = grade_in_Subj_Wr;
	}

	public String getGrade_in_Subj_Math() {
		return grade_in_Subj_Math;
	}

	public void setGrade_in_Subj_Math(String grade_in_Subj_Math) {
		this.grade_in_Subj_Math = grade_in_Subj_Math;
	}

	public String getGrade_in_Subj_Sc() {
		return grade_in_Subj_Sc;
	}

	public void setGrade_in_Subj_Sc(String grade_in_Subj_Sc) {
		this.grade_in_Subj_Sc = grade_in_Subj_Sc;
	}

	public String getGrade_in_Subj_SS() {
		return grade_in_Subj_SS;
	}

	public void setGrade_in_Subj_SS(String grade_in_Subj_SS) {
		this.grade_in_Subj_SS = grade_in_Subj_SS;
	}

	public String getTestFormat_Braille() {
		return testFormat_Braille;
	}

	public void setTestFormat_Braille(String testFormat_Braille) {
		this.testFormat_Braille = testFormat_Braille;
	}

	public String getTestFormat_LP() {
		return testFormat_LP;
	}

	public void setTestFormat_LP(String testFormat_LP) {
		this.testFormat_LP = testFormat_LP;
	}

	public String getLocal_Field_1() {
		return local_Field_1;
	}

	public void setLocal_Field_1(String local_Field_1) {
		this.local_Field_1 = local_Field_1;
	}

	public String getLocal_Field_2() {
		return local_Field_2;
	}

	public void setLocal_Field_2(String local_Field_2) {
		this.local_Field_2 = local_Field_2;
	}

	public String getLocal_Field_3() {
		return local_Field_3;
	}

	public void setLocal_Field_3(String local_Field_3) {
		this.local_Field_3 = local_Field_3;
	}

	public String getLocal_Field_4() {
		return local_Field_4;
	}

	public void setLocal_Field_4(String local_Field_4) {
		this.local_Field_4 = local_Field_4;
	}

	public String getLocal_Field_5() {
		return local_Field_5;
	}

	public void setLocal_Field_5(String local_Field_5) {
		this.local_Field_5 = local_Field_5;
	}

	public String getLocal_Field_6() {
		return local_Field_6;
	}

	public void setLocal_Field_6(String local_Field_6) {
		this.local_Field_6 = local_Field_6;
	}

	public String getLocal_Field_7() {
		return local_Field_7;
	}

	public void setLocal_Field_7(String local_Field_7) {
		this.local_Field_7 = local_Field_7;
	}

	public String getLocal_Field_8() {
		return local_Field_8;
	}

	public void setLocal_Field_8(String local_Field_8) {
		this.local_Field_8 = local_Field_8;
	}

	public String getLocal_Field_9() {
		return local_Field_9;
	}

	public void setLocal_Field_9(String local_Field_9) {
		this.local_Field_9 = local_Field_9;
	}

	public String getLocal_Field_10() {
		return local_Field_10;
	}

	public void setLocal_Field_10(String local_Field_10) {
		this.local_Field_10 = local_Field_10;
	}

	public String getLocal_Field_11() {
		return local_Field_11;
	}

	public void setLocal_Field_11(String local_Field_11) {
		this.local_Field_11 = local_Field_11;
	}

	public String getLocal_Field_12() {
		return local_Field_12;
	}

	public void setLocal_Field_12(String local_Field_12) {
		this.local_Field_12 = local_Field_12;
	}

	public String getLocal_Field_13() {
		return local_Field_13;
	}

	public void setLocal_Field_13(String local_Field_13) {
		this.local_Field_13 = local_Field_13;
	}

	public String getLocal_Field_14() {
		return local_Field_14;
	}

	public void setLocal_Field_14(String local_Field_14) {
		this.local_Field_14 = local_Field_14;
	}

	public String getLocal_Field_15() {
		return local_Field_15;
	}

	public void setLocal_Field_15(String local_Field_15) {
		this.local_Field_15 = local_Field_15;
	}

	public String getLocal_Field_16() {
		return local_Field_16;
	}

	public void setLocal_Field_16(String local_Field_16) {
		this.local_Field_16 = local_Field_16;
	}

	public String getLocal_Field_17() {
		return local_Field_17;
	}

	public void setLocal_Field_17(String local_Field_17) {
		this.local_Field_17 = local_Field_17;
	}

	public String getLocal_Field_18() {
		return local_Field_18;
	}

	public void setLocal_Field_18(String local_Field_18) {
		this.local_Field_18 = local_Field_18;
	}

	public String getLocal_Field_19() {
		return local_Field_19;
	}

	public void setLocal_Field_19(String local_Field_19) {
		this.local_Field_19 = local_Field_19;
	}

	public String getLocal_Field_20() {
		return local_Field_20;
	}

	public void setLocal_Field_20(String local_Field_20) {
		this.local_Field_20 = local_Field_20;
	}

	public String getCandidate_Acknowledgement() {
		return candidate_Acknowledgement;
	}

	public void setCandidate_Acknowledgement(String candidate_Acknowledgement) {
		this.candidate_Acknowledgement = candidate_Acknowledgement;
	}

	public String getReading_dateTestTaken() {
		return reading_dateTestTaken;
	}

	public void setReading_dateTestTaken(String reading_dateTestTaken) {
		this.reading_dateTestTaken = reading_dateTestTaken;
	}

	public String getReading_numberCorrect() {
		return reading_numberCorrect;
	}

	public void setReading_numberCorrect(String reading_numberCorrect) {
		this.reading_numberCorrect = reading_numberCorrect;
	}

	public String getReading_scaleScore() {
		return reading_scaleScore;
	}

	public void setReading_scaleScore(String reading_scaleScore) {
		this.reading_scaleScore = reading_scaleScore;
	}

	public String getReading_hSE_Score() {
		return reading_hSE_Score;
	}

	public void setReading_hSE_Score(String reading_hSE_Score) {
		this.reading_hSE_Score = reading_hSE_Score;
	}

	public String getReading_percentileRank() {
		return reading_percentileRank;
	}

	public void setReading_percentileRank(String reading_percentileRank) {
		this.reading_percentileRank = reading_percentileRank;
	}

	public String getReading_nCE() {
		return reading_nCE;
	}

	public void setReading_nCE(String reading_nCE) {
		this.reading_nCE = reading_nCE;
	}

	public String getReading_std_Obj_Mstry_Scr_1() {
		return reading_std_Obj_Mstry_Scr_1;
	}

	public void setReading_std_Obj_Mstry_Scr_1(
			String reading_std_Obj_Mstry_Scr_1) {
		this.reading_std_Obj_Mstry_Scr_1 = reading_std_Obj_Mstry_Scr_1;
	}

	public String getReading_not_All_items_atmpt_1() {
		return reading_not_All_items_atmpt_1;
	}

	public void setReading_not_All_items_atmpt_1(
			String reading_not_All_items_atmpt_1) {
		this.reading_not_All_items_atmpt_1 = reading_not_All_items_atmpt_1;
	}

	public String getReading_std_Obj_Mstry_Scr_2() {
		return reading_std_Obj_Mstry_Scr_2;
	}

	public void setReading_std_Obj_Mstry_Scr_2(
			String reading_std_Obj_Mstry_Scr_2) {
		this.reading_std_Obj_Mstry_Scr_2 = reading_std_Obj_Mstry_Scr_2;
	}

	public String getReading_not_All_items_atmpt_2() {
		return reading_not_All_items_atmpt_2;
	}

	public void setReading_not_All_items_atmpt_2(
			String reading_not_All_items_atmpt_2) {
		this.reading_not_All_items_atmpt_2 = reading_not_All_items_atmpt_2;
	}

	public String getReading_std_Obj_Mstry_Scr_3() {
		return reading_std_Obj_Mstry_Scr_3;
	}

	public void setReading_std_Obj_Mstry_Scr_3(
			String reading_std_Obj_Mstry_Scr_3) {
		this.reading_std_Obj_Mstry_Scr_3 = reading_std_Obj_Mstry_Scr_3;
	}

	public String getReading_not_All_items_atmpt_3() {
		return reading_not_All_items_atmpt_3;
	}

	public void setReading_not_All_items_atmpt_3(
			String reading_not_All_items_atmpt_3) {
		this.reading_not_All_items_atmpt_3 = reading_not_All_items_atmpt_3;
	}

	public String getReading_std_Obj_Mstry_Scr_4() {
		return reading_std_Obj_Mstry_Scr_4;
	}

	public void setReading_std_Obj_Mstry_Scr_4(
			String reading_std_Obj_Mstry_Scr_4) {
		this.reading_std_Obj_Mstry_Scr_4 = reading_std_Obj_Mstry_Scr_4;
	}

	public String getReading_not_All_items_atmpt_4() {
		return reading_not_All_items_atmpt_4;
	}

	public void setReading_not_All_items_atmpt_4(
			String reading_not_All_items_atmpt_4) {
		this.reading_not_All_items_atmpt_4 = reading_not_All_items_atmpt_4;
	}

	public String getReading_std_Obj_Mstry_Scr_5() {
		return reading_std_Obj_Mstry_Scr_5;
	}

	public void setReading_std_Obj_Mstry_Scr_5(
			String reading_std_Obj_Mstry_Scr_5) {
		this.reading_std_Obj_Mstry_Scr_5 = reading_std_Obj_Mstry_Scr_5;
	}

	public String getReading_not_All_items_atmpt_5() {
		return reading_not_All_items_atmpt_5;
	}

	public void setReading_not_All_items_atmpt_5(
			String reading_not_All_items_atmpt_5) {
		this.reading_not_All_items_atmpt_5 = reading_not_All_items_atmpt_5;
	}

	public String getReading_std_Obj_Mstry_Scr_6() {
		return reading_std_Obj_Mstry_Scr_6;
	}

	public void setReading_std_Obj_Mstry_Scr_6(
			String reading_std_Obj_Mstry_Scr_6) {
		this.reading_std_Obj_Mstry_Scr_6 = reading_std_Obj_Mstry_Scr_6;
	}

	public String getReading_not_All_items_atmpt_6() {
		return reading_not_All_items_atmpt_6;
	}

	public void setReading_not_All_items_atmpt_6(
			String reading_not_All_items_atmpt_6) {
		this.reading_not_All_items_atmpt_6 = reading_not_All_items_atmpt_6;
	}

	public String getReading_std_Obj_Mstry_Scr_7() {
		return reading_std_Obj_Mstry_Scr_7;
	}

	public void setReading_std_Obj_Mstry_Scr_7(
			String reading_std_Obj_Mstry_Scr_7) {
		this.reading_std_Obj_Mstry_Scr_7 = reading_std_Obj_Mstry_Scr_7;
	}

	public String getReading_not_All_items_atmpt_7() {
		return reading_not_All_items_atmpt_7;
	}

	public void setReading_not_All_items_atmpt_7(
			String reading_not_All_items_atmpt_7) {
		this.reading_not_All_items_atmpt_7 = reading_not_All_items_atmpt_7;
	}

	public String getReading_std_Obj_Mstry_Scr_8() {
		return reading_std_Obj_Mstry_Scr_8;
	}

	public void setReading_std_Obj_Mstry_Scr_8(
			String reading_std_Obj_Mstry_Scr_8) {
		this.reading_std_Obj_Mstry_Scr_8 = reading_std_Obj_Mstry_Scr_8;
	}

	public String getReading_not_All_items_atmpt_8() {
		return reading_not_All_items_atmpt_8;
	}

	public void setReading_not_All_items_atmpt_8(
			String reading_not_All_items_atmpt_8) {
		this.reading_not_All_items_atmpt_8 = reading_not_All_items_atmpt_8;
	}

	public String getWriting_dateTestTaken() {
		return writing_dateTestTaken;
	}

	public void setWriting_dateTestTaken(String writing_dateTestTaken) {
		this.writing_dateTestTaken = writing_dateTestTaken;
	}

	public String getWriting_numberCorrect() {
		return writing_numberCorrect;
	}

	public void setWriting_numberCorrect(String writing_numberCorrect) {
		this.writing_numberCorrect = writing_numberCorrect;
	}

	public String getWriting_scaleScore() {
		return writing_scaleScore;
	}

	public void setWriting_scaleScore(String writing_scaleScore) {
		this.writing_scaleScore = writing_scaleScore;
	}

	public String getWriting_hSE_Score() {
		return writing_hSE_Score;
	}

	public void setWriting_hSE_Score(String writing_hSE_Score) {
		this.writing_hSE_Score = writing_hSE_Score;
	}

	public String getWriting_percentileRank() {
		return writing_percentileRank;
	}

	public void setWriting_percentileRank(String writing_percentileRank) {
		this.writing_percentileRank = writing_percentileRank;
	}

	public String getWriting_nCE() {
		return writing_nCE;
	}

	public void setWriting_nCE(String writing_nCE) {
		this.writing_nCE = writing_nCE;
	}

	public String getWriting_std_Obj_Mstry_Scr_1() {
		return writing_std_Obj_Mstry_Scr_1;
	}

	public void setWriting_std_Obj_Mstry_Scr_1(
			String writing_std_Obj_Mstry_Scr_1) {
		this.writing_std_Obj_Mstry_Scr_1 = writing_std_Obj_Mstry_Scr_1;
	}

	public String getWriting_not_All_items_atmpt_1() {
		return writing_not_All_items_atmpt_1;
	}

	public void setWriting_not_All_items_atmpt_1(
			String writing_not_All_items_atmpt_1) {
		this.writing_not_All_items_atmpt_1 = writing_not_All_items_atmpt_1;
	}

	public String getWriting_std_Obj_Mstry_Scr_2() {
		return writing_std_Obj_Mstry_Scr_2;
	}

	public void setWriting_std_Obj_Mstry_Scr_2(
			String writing_std_Obj_Mstry_Scr_2) {
		this.writing_std_Obj_Mstry_Scr_2 = writing_std_Obj_Mstry_Scr_2;
	}

	public String getWriting_not_All_items_atmpt_2() {
		return writing_not_All_items_atmpt_2;
	}

	public void setWriting_not_All_items_atmpt_2(
			String writing_not_All_items_atmpt_2) {
		this.writing_not_All_items_atmpt_2 = writing_not_All_items_atmpt_2;
	}

	public String getWriting_std_Obj_Mstry_Scr_3() {
		return writing_std_Obj_Mstry_Scr_3;
	}

	public void setWriting_std_Obj_Mstry_Scr_3(
			String writing_std_Obj_Mstry_Scr_3) {
		this.writing_std_Obj_Mstry_Scr_3 = writing_std_Obj_Mstry_Scr_3;
	}

	public String getWriting_not_All_items_atmpt_3() {
		return writing_not_All_items_atmpt_3;
	}

	public void setWriting_not_All_items_atmpt_3(
			String writing_not_All_items_atmpt_3) {
		this.writing_not_All_items_atmpt_3 = writing_not_All_items_atmpt_3;
	}

	public String getWriting_std_Obj_Mstry_Scr_4() {
		return writing_std_Obj_Mstry_Scr_4;
	}

	public void setWriting_std_Obj_Mstry_Scr_4(
			String writing_std_Obj_Mstry_Scr_4) {
		this.writing_std_Obj_Mstry_Scr_4 = writing_std_Obj_Mstry_Scr_4;
	}

	public String getWriting_not_All_items_atmpt_4() {
		return writing_not_All_items_atmpt_4;
	}

	public void setWriting_not_All_items_atmpt_4(
			String writing_not_All_items_atmpt_4) {
		this.writing_not_All_items_atmpt_4 = writing_not_All_items_atmpt_4;
	}

	public String getEla_scaleScore() {
		return ela_scaleScore;
	}

	public void setEla_scaleScore(String ela_scaleScore) {
		this.ela_scaleScore = ela_scaleScore;
	}

	public String getEla_hSE_Score() {
		return ela_hSE_Score;
	}

	public void setEla_hSE_Score(String ela_hSE_Score) {
		this.ela_hSE_Score = ela_hSE_Score;
	}

	public String getEla_percentileRank() {
		return ela_percentileRank;
	}

	public void setEla_percentileRank(String ela_percentileRank) {
		this.ela_percentileRank = ela_percentileRank;
	}

	public String getEla_nCE() {
		return ela_nCE;
	}

	public void setEla_nCE(String ela_nCE) {
		this.ela_nCE = ela_nCE;
	}

	public String getMath_dateTestTaken() {
		return math_dateTestTaken;
	}

	public void setMath_dateTestTaken(String math_dateTestTaken) {
		this.math_dateTestTaken = math_dateTestTaken;
	}

	public String getMath_numberCorrect() {
		return math_numberCorrect;
	}

	public void setMath_numberCorrect(String math_numberCorrect) {
		this.math_numberCorrect = math_numberCorrect;
	}

	public String getMath_scaleScore() {
		return math_scaleScore;
	}

	public void setMath_scaleScore(String math_scaleScore) {
		this.math_scaleScore = math_scaleScore;
	}

	public String getMath_hSE_Score() {
		return math_hSE_Score;
	}

	public void setMath_hSE_Score(String math_hSE_Score) {
		this.math_hSE_Score = math_hSE_Score;
	}

	public String getMath_percentileRank() {
		return math_percentileRank;
	}

	public void setMath_percentileRank(String math_percentileRank) {
		this.math_percentileRank = math_percentileRank;
	}

	public String getMath_nCE() {
		return math_nCE;
	}

	public void setMath_nCE(String math_nCE) {
		this.math_nCE = math_nCE;
	}

	public String getMath_std_Obj_Mstry_Scr_1() {
		return math_std_Obj_Mstry_Scr_1;
	}

	public void setMath_std_Obj_Mstry_Scr_1(String math_std_Obj_Mstry_Scr_1) {
		this.math_std_Obj_Mstry_Scr_1 = math_std_Obj_Mstry_Scr_1;
	}

	public String getMath_not_All_items_atmpt_1() {
		return math_not_All_items_atmpt_1;
	}

	public void setMath_not_All_items_atmpt_1(String math_not_All_items_atmpt_1) {
		this.math_not_All_items_atmpt_1 = math_not_All_items_atmpt_1;
	}

	public String getMath_std_Obj_Mstry_Scr_2() {
		return math_std_Obj_Mstry_Scr_2;
	}

	public void setMath_std_Obj_Mstry_Scr_2(String math_std_Obj_Mstry_Scr_2) {
		this.math_std_Obj_Mstry_Scr_2 = math_std_Obj_Mstry_Scr_2;
	}

	public String getMath_not_All_items_atmpt_2() {
		return math_not_All_items_atmpt_2;
	}

	public void setMath_not_All_items_atmpt_2(String math_not_All_items_atmpt_2) {
		this.math_not_All_items_atmpt_2 = math_not_All_items_atmpt_2;
	}

	public String getMath_std_Obj_Mstry_Scr_3() {
		return math_std_Obj_Mstry_Scr_3;
	}

	public void setMath_std_Obj_Mstry_Scr_3(String math_std_Obj_Mstry_Scr_3) {
		this.math_std_Obj_Mstry_Scr_3 = math_std_Obj_Mstry_Scr_3;
	}

	public String getMath_not_All_items_atmpt_3() {
		return math_not_All_items_atmpt_3;
	}

	public void setMath_not_All_items_atmpt_3(String math_not_All_items_atmpt_3) {
		this.math_not_All_items_atmpt_3 = math_not_All_items_atmpt_3;
	}

	public String getMath_std_Obj_Mstry_Scr_4() {
		return math_std_Obj_Mstry_Scr_4;
	}

	public void setMath_std_Obj_Mstry_Scr_4(String math_std_Obj_Mstry_Scr_4) {
		this.math_std_Obj_Mstry_Scr_4 = math_std_Obj_Mstry_Scr_4;
	}

	public String getMath_not_All_items_atmpt_4() {
		return math_not_All_items_atmpt_4;
	}

	public void setMath_not_All_items_atmpt_4(String math_not_All_items_atmpt_4) {
		this.math_not_All_items_atmpt_4 = math_not_All_items_atmpt_4;
	}

	public String getMath_std_Obj_Mstry_Scr_5() {
		return math_std_Obj_Mstry_Scr_5;
	}

	public void setMath_std_Obj_Mstry_Scr_5(String math_std_Obj_Mstry_Scr_5) {
		this.math_std_Obj_Mstry_Scr_5 = math_std_Obj_Mstry_Scr_5;
	}

	public String getMath_not_All_items_atmpt_5() {
		return math_not_All_items_atmpt_5;
	}

	public void setMath_not_All_items_atmpt_5(String math_not_All_items_atmpt_5) {
		this.math_not_All_items_atmpt_5 = math_not_All_items_atmpt_5;
	}

	public String getMath_std_Obj_Mstry_Scr_6() {
		return math_std_Obj_Mstry_Scr_6;
	}

	public void setMath_std_Obj_Mstry_Scr_6(String math_std_Obj_Mstry_Scr_6) {
		this.math_std_Obj_Mstry_Scr_6 = math_std_Obj_Mstry_Scr_6;
	}

	public String getMath_not_All_items_atmpt_6() {
		return math_not_All_items_atmpt_6;
	}

	public void setMath_not_All_items_atmpt_6(String math_not_All_items_atmpt_6) {
		this.math_not_All_items_atmpt_6 = math_not_All_items_atmpt_6;
	}

	public String getMath_std_Obj_Mstry_Scr_7() {
		return math_std_Obj_Mstry_Scr_7;
	}

	public void setMath_std_Obj_Mstry_Scr_7(String math_std_Obj_Mstry_Scr_7) {
		this.math_std_Obj_Mstry_Scr_7 = math_std_Obj_Mstry_Scr_7;
	}

	public String getMath_not_All_items_atmpt_7() {
		return math_not_All_items_atmpt_7;
	}

	public void setMath_not_All_items_atmpt_7(String math_not_All_items_atmpt_7) {
		this.math_not_All_items_atmpt_7 = math_not_All_items_atmpt_7;
	}

	public String getScience_dateTestTaken() {
		return science_dateTestTaken;
	}

	public void setScience_dateTestTaken(String science_dateTestTaken) {
		this.science_dateTestTaken = science_dateTestTaken;
	}

	public String getScience_numberCorrect() {
		return science_numberCorrect;
	}

	public void setScience_numberCorrect(String science_numberCorrect) {
		this.science_numberCorrect = science_numberCorrect;
	}

	public String getScience_scaleScore() {
		return science_scaleScore;
	}

	public void setScience_scaleScore(String science_scaleScore) {
		this.science_scaleScore = science_scaleScore;
	}

	public String getScience_hSE_Score() {
		return science_hSE_Score;
	}

	public void setScience_hSE_Score(String science_hSE_Score) {
		this.science_hSE_Score = science_hSE_Score;
	}

	public String getScience_percentileRank() {
		return science_percentileRank;
	}

	public void setScience_percentileRank(String science_percentileRank) {
		this.science_percentileRank = science_percentileRank;
	}

	public String getScience_nCE() {
		return science_nCE;
	}

	public void setScience_nCE(String science_nCE) {
		this.science_nCE = science_nCE;
	}

	public String getScience_std_Obj_Mstry_Scr_1() {
		return science_std_Obj_Mstry_Scr_1;
	}

	public void setScience_std_Obj_Mstry_Scr_1(
			String science_std_Obj_Mstry_Scr_1) {
		this.science_std_Obj_Mstry_Scr_1 = science_std_Obj_Mstry_Scr_1;
	}

	public String getScience_not_All_items_atmpt_1() {
		return science_not_All_items_atmpt_1;
	}

	public void setScience_not_All_items_atmpt_1(
			String science_not_All_items_atmpt_1) {
		this.science_not_All_items_atmpt_1 = science_not_All_items_atmpt_1;
	}

	public String getScience_std_Obj_Mstry_Scr_2() {
		return science_std_Obj_Mstry_Scr_2;
	}

	public void setScience_std_Obj_Mstry_Scr_2(
			String science_std_Obj_Mstry_Scr_2) {
		this.science_std_Obj_Mstry_Scr_2 = science_std_Obj_Mstry_Scr_2;
	}

	public String getScience_not_All_items_atmpt_2() {
		return science_not_All_items_atmpt_2;
	}

	public void setScience_not_All_items_atmpt_2(
			String science_not_All_items_atmpt_2) {
		this.science_not_All_items_atmpt_2 = science_not_All_items_atmpt_2;
	}

	public String getScience_std_Obj_Mstry_Scr_3() {
		return science_std_Obj_Mstry_Scr_3;
	}

	public void setScience_std_Obj_Mstry_Scr_3(
			String science_std_Obj_Mstry_Scr_3) {
		this.science_std_Obj_Mstry_Scr_3 = science_std_Obj_Mstry_Scr_3;
	}

	public String getScience_not_All_items_atmpt_3() {
		return science_not_All_items_atmpt_3;
	}

	public void setScience_not_All_items_atmpt_3(
			String science_not_All_items_atmpt_3) {
		this.science_not_All_items_atmpt_3 = science_not_All_items_atmpt_3;
	}

	public String getScience_std_Obj_Mstry_Scr_4() {
		return science_std_Obj_Mstry_Scr_4;
	}

	public void setScience_std_Obj_Mstry_Scr_4(
			String science_std_Obj_Mstry_Scr_4) {
		this.science_std_Obj_Mstry_Scr_4 = science_std_Obj_Mstry_Scr_4;
	}

	public String getScience_not_All_items_atmpt_4() {
		return science_not_All_items_atmpt_4;
	}

	public void setScience_not_All_items_atmpt_4(
			String science_not_All_items_atmpt_4) {
		this.science_not_All_items_atmpt_4 = science_not_All_items_atmpt_4;
	}

	public String getScience_std_Obj_Mstry_Scr_5() {
		return science_std_Obj_Mstry_Scr_5;
	}

	public void setScience_std_Obj_Mstry_Scr_5(
			String science_std_Obj_Mstry_Scr_5) {
		this.science_std_Obj_Mstry_Scr_5 = science_std_Obj_Mstry_Scr_5;
	}

	public String getScience_not_All_items_atmpt_5() {
		return science_not_All_items_atmpt_5;
	}

	public void setScience_not_All_items_atmpt_5(
			String science_not_All_items_atmpt_5) {
		this.science_not_All_items_atmpt_5 = science_not_All_items_atmpt_5;
	}

	public String getSocial_dateTestTaken() {
		return social_dateTestTaken;
	}

	public void setSocial_dateTestTaken(String social_dateTestTaken) {
		this.social_dateTestTaken = social_dateTestTaken;
	}

	public String getSocial_numberCorrect() {
		return social_numberCorrect;
	}

	public void setSocial_numberCorrect(String social_numberCorrect) {
		this.social_numberCorrect = social_numberCorrect;
	}

	public String getSocial_scaleScore() {
		return social_scaleScore;
	}

	public void setSocial_scaleScore(String social_scaleScore) {
		this.social_scaleScore = social_scaleScore;
	}

	public String getSocial_hSE_Score() {
		return social_hSE_Score;
	}

	public void setSocial_hSE_Score(String social_hSE_Score) {
		this.social_hSE_Score = social_hSE_Score;
	}

	public String getSocial_percentileRank() {
		return social_percentileRank;
	}

	public void setSocial_percentileRank(String social_percentileRank) {
		this.social_percentileRank = social_percentileRank;
	}

	public String getSocial_nCE() {
		return social_nCE;
	}

	public void setSocial_nCE(String social_nCE) {
		this.social_nCE = social_nCE;
	}

	public String getSocial_std_Obj_Mstry_Scr_1() {
		return social_std_Obj_Mstry_Scr_1;
	}

	public void setSocial_std_Obj_Mstry_Scr_1(String social_std_Obj_Mstry_Scr_1) {
		this.social_std_Obj_Mstry_Scr_1 = social_std_Obj_Mstry_Scr_1;
	}

	public String getSocial_not_All_items_atmpt_1() {
		return social_not_All_items_atmpt_1;
	}

	public void setSocial_not_All_items_atmpt_1(
			String social_not_All_items_atmpt_1) {
		this.social_not_All_items_atmpt_1 = social_not_All_items_atmpt_1;
	}

	public String getSocial_std_Obj_Mstry_Scr_2() {
		return social_std_Obj_Mstry_Scr_2;
	}

	public void setSocial_std_Obj_Mstry_Scr_2(String social_std_Obj_Mstry_Scr_2) {
		this.social_std_Obj_Mstry_Scr_2 = social_std_Obj_Mstry_Scr_2;
	}

	public String getSocial_not_All_items_atmpt_2() {
		return social_not_All_items_atmpt_2;
	}

	public void setSocial_not_All_items_atmpt_2(
			String social_not_All_items_atmpt_2) {
		this.social_not_All_items_atmpt_2 = social_not_All_items_atmpt_2;
	}

	public String getSocial_std_Obj_Mstry_Scr_3() {
		return social_std_Obj_Mstry_Scr_3;
	}

	public void setSocial_std_Obj_Mstry_Scr_3(String social_std_Obj_Mstry_Scr_3) {
		this.social_std_Obj_Mstry_Scr_3 = social_std_Obj_Mstry_Scr_3;
	}

	public String getSocial_not_All_items_atmpt_3() {
		return social_not_All_items_atmpt_3;
	}

	public void setSocial_not_All_items_atmpt_3(
			String social_not_All_items_atmpt_3) {
		this.social_not_All_items_atmpt_3 = social_not_All_items_atmpt_3;
	}

	public String getSocial_std_Obj_Mstry_Scr_4() {
		return social_std_Obj_Mstry_Scr_4;
	}

	public void setSocial_std_Obj_Mstry_Scr_4(String social_std_Obj_Mstry_Scr_4) {
		this.social_std_Obj_Mstry_Scr_4 = social_std_Obj_Mstry_Scr_4;
	}

	public String getSocial_not_All_items_atmpt_4() {
		return social_not_All_items_atmpt_4;
	}

	public void setSocial_not_All_items_atmpt_4(
			String social_not_All_items_atmpt_4) {
		this.social_not_All_items_atmpt_4 = social_not_All_items_atmpt_4;
	}

	public String getSocial_std_Obj_Mstry_Scr_5() {
		return social_std_Obj_Mstry_Scr_5;
	}

	public void setSocial_std_Obj_Mstry_Scr_5(String social_std_Obj_Mstry_Scr_5) {
		this.social_std_Obj_Mstry_Scr_5 = social_std_Obj_Mstry_Scr_5;
	}

	public String getSocial_not_All_items_atmpt_5() {
		return social_not_All_items_atmpt_5;
	}

	public void setSocial_not_All_items_atmpt_5(
			String social_not_All_items_atmpt_5) {
		this.social_not_All_items_atmpt_5 = social_not_All_items_atmpt_5;
	}

	public String getSocial_std_Obj_Mstry_Scr_6() {
		return social_std_Obj_Mstry_Scr_6;
	}

	public void setSocial_std_Obj_Mstry_Scr_6(String social_std_Obj_Mstry_Scr_6) {
		this.social_std_Obj_Mstry_Scr_6 = social_std_Obj_Mstry_Scr_6;
	}

	public String getSocial_not_All_items_atmpt_6() {
		return social_not_All_items_atmpt_6;
	}

	public void setSocial_not_All_items_atmpt_6(
			String social_not_All_items_atmpt_6) {
		this.social_not_All_items_atmpt_6 = social_not_All_items_atmpt_6;
	}

	public String getSocial_std_Obj_Mstry_Scr_7() {
		return social_std_Obj_Mstry_Scr_7;
	}

	public void setSocial_std_Obj_Mstry_Scr_7(String social_std_Obj_Mstry_Scr_7) {
		this.social_std_Obj_Mstry_Scr_7 = social_std_Obj_Mstry_Scr_7;
	}

	public String getSocial_not_All_items_atmpt_7() {
		return social_not_All_items_atmpt_7;
	}

	public void setSocial_not_All_items_atmpt_7(
			String social_not_All_items_atmpt_7) {
		this.social_not_All_items_atmpt_7 = social_not_All_items_atmpt_7;
	}

	public String getOverall_scaleScore() {
		return overall_scaleScore;
	}

	public void setOverall_scaleScore(String overall_scaleScore) {
		this.overall_scaleScore = overall_scaleScore;
	}

	public String getOverall_hSE_Score() {
		return overall_hSE_Score;
	}

	public void setOverall_hSE_Score(String overall_hSE_Score) {
		this.overall_hSE_Score = overall_hSE_Score;
	}

	public String getOverall_percentileRank() {
		return overall_percentileRank;
	}

	public void setOverall_percentileRank(String overall_percentileRank) {
		this.overall_percentileRank = overall_percentileRank;
	}

	public String getOverall_nCE() {
		return overall_nCE;
	}

	public void setOverall_nCE(String overall_nCE) {
		this.overall_nCE = overall_nCE;
	}

	public String getOasReading_screenReader() {
		return oasReading_screenReader;
	}

	public void setOasReading_screenReader(String oasReading_screenReader) {
		this.oasReading_screenReader = oasReading_screenReader;
	}

	public String getOasReading_onlineCalc() {
		return oasReading_onlineCalc;
	}

	public void setOasReading_onlineCalc(String oasReading_onlineCalc) {
		this.oasReading_onlineCalc = oasReading_onlineCalc;
	}

	public String getOasReading_testPause() {
		return oasReading_testPause;
	}

	public void setOasReading_testPause(String oasReading_testPause) {
		this.oasReading_testPause = oasReading_testPause;
	}

	public String getOasReading_highlighter() {
		return oasReading_highlighter;
	}

	public void setOasReading_highlighter(String oasReading_highlighter) {
		this.oasReading_highlighter = oasReading_highlighter;
	}

	public String getOasReading_blockingRuler() {
		return oasReading_blockingRuler;
	}

	public void setOasReading_blockingRuler(String oasReading_blockingRuler) {
		this.oasReading_blockingRuler = oasReading_blockingRuler;
	}

	public String getOasReading_magnifyingGlass() {
		return oasReading_magnifyingGlass;
	}

	public void setOasReading_magnifyingGlass(String oasReading_magnifyingGlass) {
		this.oasReading_magnifyingGlass = oasReading_magnifyingGlass;
	}

	public String getOasReading_fontAndBkgndClr() {
		return oasReading_fontAndBkgndClr;
	}

	public void setOasReading_fontAndBkgndClr(String oasReading_fontAndBkgndClr) {
		this.oasReading_fontAndBkgndClr = oasReading_fontAndBkgndClr;
	}

	public String getOasReading_largeFont() {
		return oasReading_largeFont;
	}

	public void setOasReading_largeFont(String oasReading_largeFont) {
		this.oasReading_largeFont = oasReading_largeFont;
	}

	public String getOasReading_musicPlayer() {
		return oasReading_musicPlayer;
	}

	public void setOasReading_musicPlayer(String oasReading_musicPlayer) {
		this.oasReading_musicPlayer = oasReading_musicPlayer;
	}

	public String getOasReading_extendedTime() {
		return oasReading_extendedTime;
	}

	public void setOasReading_extendedTime(String oasReading_extendedTime) {
		this.oasReading_extendedTime = oasReading_extendedTime;
	}

	public String getOasReading_maskingTool() {
		return oasReading_maskingTool;
	}

	public void setOasReading_maskingTool(String oasReading_maskingTool) {
		this.oasReading_maskingTool = oasReading_maskingTool;
	}

	public String getOasWriting_screenReader() {
		return oasWriting_screenReader;
	}

	public void setOasWriting_screenReader(String oasWriting_screenReader) {
		this.oasWriting_screenReader = oasWriting_screenReader;
	}

	public String getOasWriting_onlineCalc() {
		return oasWriting_onlineCalc;
	}

	public void setOasWriting_onlineCalc(String oasWriting_onlineCalc) {
		this.oasWriting_onlineCalc = oasWriting_onlineCalc;
	}

	public String getOasWriting_testPause() {
		return oasWriting_testPause;
	}

	public void setOasWriting_testPause(String oasWriting_testPause) {
		this.oasWriting_testPause = oasWriting_testPause;
	}

	public String getOasWriting_highlighter() {
		return oasWriting_highlighter;
	}

	public void setOasWriting_highlighter(String oasWriting_highlighter) {
		this.oasWriting_highlighter = oasWriting_highlighter;
	}

	public String getOasWriting_blockingRuler() {
		return oasWriting_blockingRuler;
	}

	public void setOasWriting_blockingRuler(String oasWriting_blockingRuler) {
		this.oasWriting_blockingRuler = oasWriting_blockingRuler;
	}

	public String getOasWriting_magnifyingGlass() {
		return oasWriting_magnifyingGlass;
	}

	public void setOasWriting_magnifyingGlass(String oasWriting_magnifyingGlass) {
		this.oasWriting_magnifyingGlass = oasWriting_magnifyingGlass;
	}

	public String getOasWriting_fontAndBkgndClr() {
		return oasWriting_fontAndBkgndClr;
	}

	public void setOasWriting_fontAndBkgndClr(String oasWriting_fontAndBkgndClr) {
		this.oasWriting_fontAndBkgndClr = oasWriting_fontAndBkgndClr;
	}

	public String getOasWriting_largeFont() {
		return oasWriting_largeFont;
	}

	public void setOasWriting_largeFont(String oasWriting_largeFont) {
		this.oasWriting_largeFont = oasWriting_largeFont;
	}

	public String getOasWriting_musicPlayer() {
		return oasWriting_musicPlayer;
	}

	public void setOasWriting_musicPlayer(String oasWriting_musicPlayer) {
		this.oasWriting_musicPlayer = oasWriting_musicPlayer;
	}

	public String getOasWriting_extendedTime() {
		return oasWriting_extendedTime;
	}

	public void setOasWriting_extendedTime(String oasWriting_extendedTime) {
		this.oasWriting_extendedTime = oasWriting_extendedTime;
	}

	public String getOasWriting_maskingTool() {
		return oasWriting_maskingTool;
	}

	public void setOasWriting_maskingTool(String oasWriting_maskingTool) {
		this.oasWriting_maskingTool = oasWriting_maskingTool;
	}

	public String getOasMath_screenReader() {
		return oasMath_screenReader;
	}

	public void setOasMath_screenReader(String oasMath_screenReader) {
		this.oasMath_screenReader = oasMath_screenReader;
	}

	public String getOasMath_onlineCalc() {
		return oasMath_onlineCalc;
	}

	public void setOasMath_onlineCalc(String oasMath_onlineCalc) {
		this.oasMath_onlineCalc = oasMath_onlineCalc;
	}

	public String getOasMath_testPause() {
		return oasMath_testPause;
	}

	public void setOasMath_testPause(String oasMath_testPause) {
		this.oasMath_testPause = oasMath_testPause;
	}

	public String getOasMath_highlighter() {
		return oasMath_highlighter;
	}

	public void setOasMath_highlighter(String oasMath_highlighter) {
		this.oasMath_highlighter = oasMath_highlighter;
	}

	public String getOasMath_blockingRuler() {
		return oasMath_blockingRuler;
	}

	public void setOasMath_blockingRuler(String oasMath_blockingRuler) {
		this.oasMath_blockingRuler = oasMath_blockingRuler;
	}

	public String getOasMath_magnifyingGlass() {
		return oasMath_magnifyingGlass;
	}

	public void setOasMath_magnifyingGlass(String oasMath_magnifyingGlass) {
		this.oasMath_magnifyingGlass = oasMath_magnifyingGlass;
	}

	public String getOasMath_fontAndBkgndClr() {
		return oasMath_fontAndBkgndClr;
	}

	public void setOasMath_fontAndBkgndClr(String oasMath_fontAndBkgndClr) {
		this.oasMath_fontAndBkgndClr = oasMath_fontAndBkgndClr;
	}

	public String getOasMath_largeFont() {
		return oasMath_largeFont;
	}

	public void setOasMath_largeFont(String oasMath_largeFont) {
		this.oasMath_largeFont = oasMath_largeFont;
	}

	public String getOasMath_musicPlayer() {
		return oasMath_musicPlayer;
	}

	public void setOasMath_musicPlayer(String oasMath_musicPlayer) {
		this.oasMath_musicPlayer = oasMath_musicPlayer;
	}

	public String getOasMath_extendedTime() {
		return oasMath_extendedTime;
	}

	public void setOasMath_extendedTime(String oasMath_extendedTime) {
		this.oasMath_extendedTime = oasMath_extendedTime;
	}

	public String getOasMath_maskingTool() {
		return oasMath_maskingTool;
	}

	public void setOasMath_maskingTool(String oasMath_maskingTool) {
		this.oasMath_maskingTool = oasMath_maskingTool;
	}

	public String getOasScience_screenReader() {
		return oasScience_screenReader;
	}

	public void setOasScience_screenReader(String oasScience_screenReader) {
		this.oasScience_screenReader = oasScience_screenReader;
	}

	public String getOasScience_onlineCalc() {
		return oasScience_onlineCalc;
	}

	public void setOasScience_onlineCalc(String oasScience_onlineCalc) {
		this.oasScience_onlineCalc = oasScience_onlineCalc;
	}

	public String getOasScience_testPause() {
		return oasScience_testPause;
	}

	public void setOasScience_testPause(String oasScience_testPause) {
		this.oasScience_testPause = oasScience_testPause;
	}

	public String getOasScience_highlighter() {
		return oasScience_highlighter;
	}

	public void setOasScience_highlighter(String oasScience_highlighter) {
		this.oasScience_highlighter = oasScience_highlighter;
	}

	public String getOasScience_blockingRuler() {
		return oasScience_blockingRuler;
	}

	public void setOasScience_blockingRuler(String oasScience_blockingRuler) {
		this.oasScience_blockingRuler = oasScience_blockingRuler;
	}

	public String getOasScience_magnifyingGlass() {
		return oasScience_magnifyingGlass;
	}

	public void setOasScience_magnifyingGlass(String oasScience_magnifyingGlass) {
		this.oasScience_magnifyingGlass = oasScience_magnifyingGlass;
	}

	public String getOasScience_fontAndBkgndClr() {
		return oasScience_fontAndBkgndClr;
	}

	public void setOasScience_fontAndBkgndClr(String oasScience_fontAndBkgndClr) {
		this.oasScience_fontAndBkgndClr = oasScience_fontAndBkgndClr;
	}

	public String getOasScience_largeFont() {
		return oasScience_largeFont;
	}

	public void setOasScience_largeFont(String oasScience_largeFont) {
		this.oasScience_largeFont = oasScience_largeFont;
	}

	public String getOasScience_musicPlayer() {
		return oasScience_musicPlayer;
	}

	public void setOasScience_musicPlayer(String oasScience_musicPlayer) {
		this.oasScience_musicPlayer = oasScience_musicPlayer;
	}

	public String getOasScience_extendedTime() {
		return oasScience_extendedTime;
	}

	public void setOasScience_extendedTime(String oasScience_extendedTime) {
		this.oasScience_extendedTime = oasScience_extendedTime;
	}

	public String getOasScience_maskingTool() {
		return oasScience_maskingTool;
	}

	public void setOasScience_maskingTool(String oasScience_maskingTool) {
		this.oasScience_maskingTool = oasScience_maskingTool;
	}

	public String getOasSocial_screenReader() {
		return oasSocial_screenReader;
	}

	public void setOasSocial_screenReader(String oasSocial_screenReader) {
		this.oasSocial_screenReader = oasSocial_screenReader;
	}

	public String getOasSocial_onlineCalc() {
		return oasSocial_onlineCalc;
	}

	public void setOasSocial_onlineCalc(String oasSocial_onlineCalc) {
		this.oasSocial_onlineCalc = oasSocial_onlineCalc;
	}

	public String getOasSocial_testPause() {
		return oasSocial_testPause;
	}

	public void setOasSocial_testPause(String oasSocial_testPause) {
		this.oasSocial_testPause = oasSocial_testPause;
	}

	public String getOasSocial_highlighter() {
		return oasSocial_highlighter;
	}

	public void setOasSocial_highlighter(String oasSocial_highlighter) {
		this.oasSocial_highlighter = oasSocial_highlighter;
	}

	public String getOasSocial_blockingRuler() {
		return oasSocial_blockingRuler;
	}

	public void setOasSocial_blockingRuler(String oasSocial_blockingRuler) {
		this.oasSocial_blockingRuler = oasSocial_blockingRuler;
	}

	public String getOasSocial_magnifyingGlass() {
		return oasSocial_magnifyingGlass;
	}

	public void setOasSocial_magnifyingGlass(String oasSocial_magnifyingGlass) {
		this.oasSocial_magnifyingGlass = oasSocial_magnifyingGlass;
	}

	public String getOasSocial_fontAndBkgndClr() {
		return oasSocial_fontAndBkgndClr;
	}

	public void setOasSocial_fontAndBkgndClr(String oasSocial_fontAndBkgndClr) {
		this.oasSocial_fontAndBkgndClr = oasSocial_fontAndBkgndClr;
	}

	public String getOasSocial_largeFont() {
		return oasSocial_largeFont;
	}

	public void setOasSocial_largeFont(String oasSocial_largeFont) {
		this.oasSocial_largeFont = oasSocial_largeFont;
	}

	public String getOasSocial_musicPlayer() {
		return oasSocial_musicPlayer;
	}

	public void setOasSocial_musicPlayer(String oasSocial_musicPlayer) {
		this.oasSocial_musicPlayer = oasSocial_musicPlayer;
	}

	public String getOasSocial_extendedTime() {
		return oasSocial_extendedTime;
	}

	public void setOasSocial_extendedTime(String oasSocial_extendedTime) {
		this.oasSocial_extendedTime = oasSocial_extendedTime;
	}

	public String getOasSocial_maskingTool() {
		return oasSocial_maskingTool;
	}

	public void setOasSocial_maskingTool(String oasSocial_maskingTool) {
		this.oasSocial_maskingTool = oasSocial_maskingTool;
	}

	public String getReadingItems_SR() {
		return readingItems_SR;
	}

	public void setReadingItems_SR(String readingItems_SR) {
		this.readingItems_SR = readingItems_SR;
	}

	public String getReadingItems_FU() {
		return readingItems_FU;
	}

	public void setReadingItems_FU(String readingItems_FU) {
		this.readingItems_FU = readingItems_FU;
	}

	public String getWritingItems_SR() {
		return writingItems_SR;
	}

	public void setWritingItems_SR(String writingItems_SR) {
		this.writingItems_SR = writingItems_SR;
	}

	public String getWritingItems_CR() {
		return writingItems_CR;
	}

	public void setWritingItems_CR(String writingItems_CR) {
		this.writingItems_CR = writingItems_CR;
	}

	public String getWritingItems_FU() {
		return writingItems_FU;
	}

	public void setWritingItems_FU(String writingItems_FU) {
		this.writingItems_FU = writingItems_FU;
	}

	public String getMathItems_SR() {
		return mathItems_SR;
	}

	public void setMathItems_SR(String mathItems_SR) {
		this.mathItems_SR = mathItems_SR;
	}

	public String getMathItems_GR_Status() {
		return mathItems_GR_Status;
	}

	public void setMathItems_GR_Status(String mathItems_GR_Status) {
		this.mathItems_GR_Status = mathItems_GR_Status;
	}

	public String getMathItems_GR_Edited() {
		return mathItems_GR_Edited;
	}

	public void setMathItems_GR_Edited(String mathItems_GR_Edited) {
		this.mathItems_GR_Edited = mathItems_GR_Edited;
	}

	public String getMathItems_FU() {
		return mathItems_FU;
	}

	public void setMathItems_FU(String mathItems_FU) {
		this.mathItems_FU = mathItems_FU;
	}

	public String getScienceItems_SR() {
		return scienceItems_SR;
	}

	public void setScienceItems_SR(String scienceItems_SR) {
		this.scienceItems_SR = scienceItems_SR;
	}

	public String getScienceItems_FU() {
		return scienceItems_FU;
	}

	public void setScienceItems_FU(String scienceItems_FU) {
		this.scienceItems_FU = scienceItems_FU;
	}

	public String getSocialStudies_SR() {
		return socialStudies_SR;
	}

	public void setSocialStudies_SR(String socialStudies_SR) {
		this.socialStudies_SR = socialStudies_SR;
	}

	public String getSocialStudies_FU() {
		return socialStudies_FU;
	}

	public void setSocialStudies_FU(String socialStudies_FU) {
		this.socialStudies_FU = socialStudies_FU;
	}

	public String getcTBUseField() {
		return cTBUseField;
	}

	public void setcTBUseField(String cTBUseField) {
		this.cTBUseField = cTBUseField;
	}
}
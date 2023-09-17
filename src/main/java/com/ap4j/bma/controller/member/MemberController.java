package com.ap4j.bma.controller.member;
import com.ap4j.bma.config.PasswordEncoderConfig;
import com.ap4j.bma.model.entity.customerCenter.QnAEntity;
import com.ap4j.bma.model.entity.meamulReg.MaemulRegEntity;
import com.ap4j.bma.model.entity.member.MemberDTO;
import com.ap4j.bma.model.entity.member.MemberEntity;
import com.ap4j.bma.service.member.LikedService;
import com.ap4j.bma.service.member.MemberService;
import com.ap4j.bma.service.member.RecentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@SessionAttributes("loginMember")   // 세션 자동 설정
@RequestMapping("/member")
@Controller
public class MemberController {

    @Autowired
    private MemberService qMemberService;

    @Autowired
    private LikedService likedService;

    @Autowired
    private RecentServiceImpl recentServiceImpl;

    @Autowired
    private PasswordEncoderConfig pwdConfig;

    /** 로그인 여부 체크 */
    public boolean loginStatus(HttpSession session) {
        return session.getAttribute("loginMember") != null;
    }

    /** 로그인멤버 가입경로 */
    public String getMemberRoot(int root) {
        return root == 1 ? "기본회원" : root == 2 ? "카카오" : root == 3 ? "네이버" : "관리자";
    }

    /** 로그인멤버 이메일 */
    public String getMemberEmail(HttpSession session) {
        MemberDTO memberDTO = (MemberDTO) session.getAttribute("loginMember");
        return memberDTO != null ? memberDTO.getEmail() : null;
    }

    /** 로그인멤버 */
    public MemberDTO getMemberInfo(HttpSession session) {
        return (MemberDTO) session.getAttribute("loginMember");
    }

    /** 로그인 페이지 매핑 */
    @RequestMapping("/qLoginForm")
    public String qLoginForm(@CookieValue(value = "rememberedEmail", required = false) String rememberedEmail, Model model, HttpSession session) {
        if(loginStatus(session)) { return "userView/loginAlready"; }

        model.addAttribute("rememberedEmail", rememberedEmail); // 쿠키가 있는 경우, 저장 이메일 표시
        return "userView/oLoginForm";
    }

    /** 카카오 로그인 실행 */
    @RequestMapping("/qLogin")
    public String qLogin(@RequestParam(value = "code", required = false) String code, Model model, HttpSession session) {
        // 1번 인증코드 요청 전달
        String accessToken = qMemberService.getAccessToken(code);     // code로 토큰 받음
        log.info("accessToken : " + accessToken);

        // 2번 인증코드로 토큰 전달
        HashMap<String, Object> userInfo = qMemberService.getUserInfo(accessToken);   // 사용자 정보 받음
        log.info("login info : " + userInfo.toString());
        log.info("login userEmail : " + userInfo.get("email"));
        log.info("login userName : " + userInfo.get("name"));
        log.info("login userNickname : " + userInfo.get("nickname"));
        log.info("login userPhone_number : " + userInfo.get("phone_number"));
        log.info("login thumbnail_image : " + userInfo.get("thumbnail_image"));

        String thumbnail_image = (String) userInfo.get("thumbnail_image");
        session.setAttribute("thumbnail_image", thumbnail_image);

        boolean emailCheck = qMemberService.existsByEmail((String) userInfo.get("email"));  // DB내 이메일 존재 여부 체크

        MemberDTO loginMember = new MemberDTO();

        if(emailCheck) {
            log.info("존재하는 이메일입니다. 로그인을 진행합니다.");    // 이미 가입된 계정이면 로그인 진행
            loginMember.setEmail((String) userInfo.get("email"));
            loginMember.setName((String) userInfo.get("name"));
            loginMember.setNickname((String) userInfo.get("nickname"));
            loginMember.setTel((String) userInfo.get("phone_number"));
            loginMember = qMemberService.login(loginMember);
            loginMember.toEntity();

            model.addAttribute("loginMember",loginMember);
            log.info("loginMember : " + loginMember.toString());

        } else {
            loginMember.setEmail((String) userInfo.get("email"));
            loginMember.setName((String) userInfo.get("name"));
            loginMember.setNickname((String) userInfo.get("nickname"));
            loginMember.setTel((String) userInfo.get("phone_number"));
            loginMember.setRoot(2);
            // 비밀번호 부분 수정해야함

            MemberEntity memberEntity = loginMember.toEntity();
            qMemberService.joinBasic(loginMember);   // 카카오정보로 회원가입 실행
            model.addAttribute("loginMember", loginMember);
        }
        return "redirect:/";
    }

    /** 로그아웃 */
    @RequestMapping("/qLogout")
    public String qLogout(SessionStatus sessionStatus, HttpSession session) {
        qMemberService.logout(sessionStatus, session);
        return "redirect:/";
    }

    /** 기본 로그인 */
    @PostMapping("/qLoginBasic")
    public String qBasicLogin(@ModelAttribute MemberDTO memberDTO,
                              @RequestParam(required = false) boolean oSaveId,
                              Model model, HttpSession session, HttpServletResponse response) {
        MemberDTO loginMember = qMemberService.login(memberDTO);

        if(loginMember != null && !loginMember.getMember_leave()) {
            session.setAttribute("errorMsg", null);
            log.info("로그인 성공");

            loginMember.toEntity();
            model.addAttribute("loginMember",  loginMember);

            // 쿠키 작업
            if(oSaveId) {
                Cookie cookie = new Cookie("rememberedEmail", loginMember.getEmail());
                cookie.setMaxAge(30 * 24 * 60 * 60);    // 30일 동안 유지
                cookie.setPath("/");                    // 모든 경로에 쿠키 설정
                response.addCookie(cookie);
            } else {
                Cookie cookie = new Cookie("rememberedEmail", null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            return "redirect:/";

        } else {
            model.addAttribute("errorMsg","이메일 또는 패스워드를 다시 확인해주세요.");
            return "userView/oLoginForm";
        }
    }

    /** 기본 회원가입 폼 */
    @RequestMapping("/qJoinForm")
    public String qJoinForm(HttpSession session) {
        if(loginStatus(session)) { return "userView/loginAlready"; }
        return "userView/oJoinForm";
    }

    /** 기본 회원가입 */
    @PostMapping("/qJoinBasic")
    public String qJoinBasic(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        memberDTO.setRoot(1);
        Long joinId = qMemberService.joinBasic(memberDTO);

        if(joinId == 0) {
            session.setAttribute("faileDTO", memberDTO);
            return "redirect:/member/qJoinForm";
        }
        return "redirect:/member/qLoginForm";
    }

    /** 네이버 로그인 */
    @RequestMapping("/qLoginNaver")
    public String qLoginNaver(@RequestParam("code") String code,
                              @RequestParam("state") String state, Model model, HttpSession session) {
        log.info("MemberController - qLoginNaver() 실행");
        log.info("####code : " + code);

        // 1번 인증코드 요청 전달
        String accessToken = qMemberService.getAccessTokenNaver(code);     // code로 토큰 받음
        log.info("accessToken : " + accessToken);

//        // 2번 인증코드로 토큰 전달
        HashMap<String, Object> userInfo = qMemberService.getUserInfoNaver(accessToken);   // 사용자 정보 받음
        log.info("login info : " + userInfo.toString());

        String profile_image = (String) userInfo.get("profile_image");
        log.info("저장한 profile_image : " + profile_image);
        session.setAttribute("thumbnail_image", profile_image);

        boolean emailCheck = qMemberService.existsByEmail((String) userInfo.get("email"));  // DB내 이메일 존재 여부 체크
        MemberDTO loginMember = new MemberDTO();

        if(emailCheck) {
            log.info("존재하는 이메일입니다. 로그인을 진행합니다.");    // 이미 가입된 계정이면 로그인 진행
            loginMember.setEmail((String) userInfo.get("email"));
            loginMember.setName((String) userInfo.get("name"));
            loginMember.setNickname((String) userInfo.get("nickname"));
            loginMember.setTel((String) userInfo.get("phone_number"));
            loginMember = qMemberService.login(loginMember);

            if(loginMember == null) { return "redirect:/member/qLoginForm"; }

            loginMember.toEntity();

            model.addAttribute("loginMember",loginMember);
            log.info("loginMember : " + loginMember);

        } else {
            loginMember.setEmail((String) userInfo.get("email"));
            loginMember.setName((String) userInfo.get("name"));
            loginMember.setNickname((String) userInfo.get("nickname"));
            loginMember.setTel((String) userInfo.get("mobile"));
            loginMember.setRoot(3);

            MemberEntity memberEntity = loginMember.toEntity();
            qMemberService.joinBasic(loginMember);
            model.addAttribute("loginMember", loginMember);
        }
        return "redirect:/";
    }

    /** 마이페이지 매핑 */ 
    @RequestMapping("/qMyPage")
    public String qMyPage(@RequestParam(name = "page", defaultValue = "1") int page,
                          @RequestParam(name = "pageSize", defaultValue = "9") int pageSize,
                          HttpSession session, Model model) {
        if(!loginStatus(session)) { return "userView/loginNeed"; }

        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        String nickname = loginMember.getNickname();
        String root = getMemberRoot(loginMember.getRoot());
        String thumImg = (String) session.getAttribute("thumbnail_image");


        Page<MaemulRegEntity> mmpList = likedService.getPaginatedItems(nickname,page,pageSize);
        Long likedCnt = likedService.countLikedByNickname(nickname);

        Page<MaemulRegEntity> recentList = recentServiceImpl.recentMaemulList(nickname,page,pageSize);
        Long recentListCnt = recentServiceImpl.recentMamulListCount(nickname);

        model.addAttribute("root", root);
        model.addAttribute("thumbnail_image", thumImg);
        model.addAttribute("likedCnt",likedCnt);
        model.addAttribute("mmpList",mmpList);
        model.addAttribute("recentList",recentList);
        model.addAttribute("recentListCnt",recentListCnt);

        return "userView/myPage";
    }

    /** 내정보 수정페이지 매핑 */
    @GetMapping("/qMyInfoUpdate")
    public String qMyInfoUpdate(HttpSession session, Model model) {
        if(!loginStatus(session)) { return "userView/loginNeed"; }
        String thumImg = (String) session.getAttribute("thumbnail_image");
        String thumImgSel = (String) session.getAttribute("selectedImage");
        if(thumImgSel != null) {
            model.addAttribute("selectedImage", thumImgSel);
        } else {
            model.addAttribute("thumbnail_image", thumImg);
        }
        return "userView/oMyInfoUpdate";
    }

    /** 내정보 수정하기 */
    @PostMapping("/qUpdateMember/{id}")
    public String qUpdate(@ModelAttribute MemberDTO updatedMember, Model model, HttpSession session) {
        if(!loginStatus(session)) { return "userView/loginNeed"; }

        Long id =  ((MemberDTO) session.getAttribute("loginMember")).getId();

        MemberEntity memberEntity = qMemberService.updateMember(id, updatedMember);
        if(memberEntity == null) {
            session.setAttribute("faileDTO", updatedMember);
            return "redirect:/member/qMyPage";
        }
        model.addAttribute("loginMember", memberEntity.toDTO());

        return "redirect:/member/qMyPage";
    }

    /** 1:1 문의내역 페이지 매핑 */
    @GetMapping("/qMyQnA")
    public String qMyQnA(@RequestParam(name = "page", defaultValue = "1") int page,
                         @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Model model, HttpSession session) {
        if(!loginStatus(session)) { return "userView/loginNeed"; }

        String userEmail = getMemberEmail(session);

        if (page < 1) {
            page = 1;
        }

        Page<QnAEntity> qMyQnaList = qMemberService.qMyQnaList(userEmail,page,pageSize);
        long cnt = qMemberService.qMyQnaListCount(userEmail);

        model.addAttribute("myQnaList", qMyQnaList);
        model.addAttribute("myQnaCnt", cnt);
        return "userView/myQnA";
    }

    /** 기본 회원탈퇴 (js ajax 활용) */
    @PostMapping("/qLeaveMember2")
    public ResponseEntity<Integer> qLeaveMember2(@RequestParam(required = false) String password,
                                                 HttpSession session, SessionStatus sessionStatus) {
        Long id =  ((MemberDTO) getMemberInfo(session)).getId();
        boolean success = qMemberService.leaveMember(id, password, sessionStatus, session);
        return ResponseEntity.ok(success ? 1 : 0);
    }

    @PostMapping("/qLeaveMember/{id}")    // id 사용해서 탈퇴 (sns계정 탈퇴)
    public String leaveMember(HttpSession session, SessionStatus sessionStatus) {
        Long id =  ((MemberDTO) getMemberInfo(session)).getId();
        String pwd =  ((MemberDTO) getMemberInfo(session)).getPwd();

        qMemberService.leaveMember(id, pwd, sessionStatus, session);

        return "redirect:/";
    }

    /** 중복회원(이메일) 체크 (js ajax 활용) */
    @PostMapping("/qEmailCheck")
    @ResponseBody
    public int qEmailCheck(@RequestParam("email") String email) {
        boolean emailCheck = qMemberService.existsByEmail(email);
        return emailCheck ? 1 : 0;
    }

    /** 닉네임 중복 체크 (js ajax 활용) */
    @PostMapping("/qNicknameDuplicationCheck")
    @ResponseBody
    public int NicknameDuplicationCheck(@RequestParam("nickname") String nickname) {
        boolean nicknameCheck = qMemberService.existsByNickname(nickname);
        return nicknameCheck ? 1 : 0;
    }

    /** 이메일/비밀번호 재설정 페이지 매핑 */
    @RequestMapping("/qFindMemberInfo")
    public String qFindMemberInfo() {
        return "userView/findMemberInfo";
    }

    /** 이메일 찾기 (ajax) */
    @PostMapping("/qFindEmail")
    public ResponseEntity<Map<String, String>> qFindEmail(@RequestParam String name, @RequestParam String tel) {
        Map<String, String> response = new HashMap<>();
        Optional<MemberEntity> findMember = qMemberService.findByNameAndTel(name, tel);

        if (findMember.isPresent()) {
            String email = findMember.get().getEmail();
            response.put("findEmail", email);
        } else {
            response.put("findEmailFailed", "일치하는 회원정보가 없습니다.");
        }

        return ResponseEntity.ok(response);
    }
}
//    @PostMapping("/qFindEmail")
//    public String qFindEmail(@RequestParam String name, @RequestParam String tel, Model model) {
//        Optional<MemberEntity> findMember = qMemberService.findByNameAndTel(name, tel);
//
//        model.addAttribute("findEmail", findMember.map(MemberEntity::getEmail).orElse(null));
//        model.addAttribute("findEmailFailed", findMember.isEmpty() ? "일치하는 회원정보가 없습니다." : null);
//
//        return "userView/findMemberInfo";
//    }


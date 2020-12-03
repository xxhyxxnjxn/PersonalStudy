<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>




<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!--**********************************
            Sidebar start
        ***********************************-->
<div class="nk-sidebar">
	<div class="nk-nav-scroll">
		<ul class="metismenu" id="menu">
			<li class="nav-label">마이페이지</li>

			<li class="mega-menu mega-menu-sm"><a class="has-arrow"
				href="javascript:void()" aria-expanded="false"> <i
					class="icon-globe-alt menu-icon"></i><span class="nav-text">투자내역</span>
			</a>
				<ul aria-expanded="false">
					<li><a href="/botCreate?m_id=${login.m_id}">봇생성</a></li>
				</ul></li>
			<c:if test="${login.m_id=='123'}">
				<li class="mega-menu mega-menu-sm"><a class="has-arrow"
					href="javascript:void()" aria-expanded="false"> <i
						class="icon-globe-alt menu-icon"></i><span class="nav-text">관리자</span>
				</a>
					<ul aria-expanded="false">
						<li><a href="/script">스크립트</a></li>
					</ul></li>

			</c:if>


		</ul>
	</div>
</div>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
                    <c:forEach var="index" begin="0" end="6">
	                    <c:set var="week" value="${weeks[index]}"/>
	                    <div id="task-week-${index+1}" class="week">
	  						<c:forEach var="task" items="${week}">
								<div class="task start-day-${task.startDay}">
								    <div class="foreseen interval-day-${task.intervalDay}">
								        <div class="stage ${task.stage}"></div>
								        <span class="task-description">${task.description}</span>
								    </div>
								    <c:forEach var="hashtagDay" items="${task.hashtagDays}">
									    <div class="hashtags task-day-${hashtagDay.day}">
											<c:forEach var="hashtag" items="${hashtagDay.hashtags}">    
										        <span class="${fn:toLowerCase(hashtag)}">&#35;${hashtag}</span>
										    </c:forEach>
									    </div>
									</c:forEach>
								</div>
							</c:forEach>                        
	                    </div>
	                </c:forEach>
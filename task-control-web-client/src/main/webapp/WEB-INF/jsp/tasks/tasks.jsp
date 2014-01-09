<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
                    <div id="task-week-1" class="week">
                        <div class="task start-day-2 interval-day-2">
                            <div class="foreseen">
                                <div class="stage finished"></div>
                                <span class="task-description">SR123456789 - Beef ribs chicken tail boudin.</span>
                            </div>
                        </div>
                        <div class="task start-day-1 interval-day-6 continue-right">
                            <div class="foreseen">
                                <div class="stage finished"></div>
                                <span class="task-description">SR123456789 - Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.</span>
                            </div>
                        </div>
                    </div>
                    <div id="task-week-2" class="week">
                        <div class="task start-day-7">
                            <div class="foreseen interval-day-1">
                                <div class="stage finished"></div>
                                <span class="task-description">SR999 - Change.</span>
                            </div>
                            <div class="hashtags task-day-1">
                                <span class="late">#atraso</span>
                                <span class="overtime">#horasextra</span>
                            </div>
                        </div>                
                        <div class="task start-day-1 interval-day-7 continue-left">
                            <div class="foreseen">
                                <div class="stage finished interval-day-4"></div>
                                <span class="task-description">SR123456789 - Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.</span>
                            </div>
                            <div class="hashtags task-day-1">
                                <span class="overtime">#horasextra</span>
                            </div>
                            <div class="hashtags task-day-2">
                                <span class="overtime">#horasextra</span>
                            </div>                     
                            <div class="hashtags task-day-3">
                                <span class="overtime">#horasextra</span>
                            </div>
                            <div class="hashtags task-day-4">
                                <span class="overtime">#horasextra</span>
                            </div>
                            <div class="hashtags task-day-5">
                                <span class="overtime">#horasextra</span>
                            </div>
                            <div class="hashtags task-day-6">
                                <span class="overtime">#horasextra</span>
                            </div>
                            <div class="hashtags task-day-7">
                                <span class="overtime">#horasextra</span>
                            </div>
                        </div>
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
                    </div>
                    <div id="task-week-3" class="week">
                        <div class="task start-day-2 interval-day-5">
                            <div class="foreseen">
                                <div class="stage interval-day-3 doing"></div>
                                <span class="task-description">SR123456789 - Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.</span>
                            </div>
                            <div class="hashtags task-day-2">
                                <span class="overtime">#horasextra</span>
                            </div>                    
                        </div>
                        <div class="task start-day-2 interval-day-3 late">
                            <div class="foreseen">
                                <div class="stage interval-day-1 doing"></div>
                                <span class="task-description">SR123456789 - Beef ribs chicken tail boudin.</span>
                            </div>
                            <div class="hashtags task-day-2">
                                <span class="overtime">#horasextra</span>
                            </div>                    
                        </div>                
                    </div>
                    <div id="task-week-4" class="week">
                        <div class="task start-day-1 interval-day-3" title="SR123456789 - Beef ribs chicken tail boudin.">
                            <div class="foreseen">
                                <div class="stage"></div>
                                <span class="task-description">SR123456789 - Beef ribs chicken tail boudin.</span>
                            </div>
                        </div>
                        <div class="task start-day-2 interval-day-2" title="SR123456789 - Beef ribs chicken tail boudin.">
                            <div class="foreseen">
                                <div class="stage"></div>
                                <span class="task-description">SR123456789 - Beef ribs chicken tail boudin.</span>
                            </div>
                        </div>
                        <div class="task start-day-3 interval-day-1 " title="SR123456789 - Beef ribs chicken tail boudin.">
                            <div class="foreseen">
                                <div class="stage"></div>
                                <span class="task-description">SR123456789 - Beef ribs chicken tail boudin.</span>
                            </div>
                        </div>                        
                    </div>
                    <div id="task-week-5" class="week">
                        <div class="task start-day-4 interval-day-1" title="SR123456789 - Beef ribs chicken tail boudin.">
                            <div class="foreseen">
                                <div class="stage"></div>
                                <span class="task-description">SR123456789 - Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.</span>
                            </div>
                        </div>
                        <div class="task start-day-5 interval-day-1" title="SR123456789 - Beef ribs chicken tail boudin.">
                            <div class="foreseen">
                                <div class="stage"></div>
                                <span class="task-description">SR123456789 - Beef ribs chicken tail boudin.</span>
                            </div>
                        </div>
                        <div class="task start-day-6 interval-day-1" title="SR123456789 - Beef ribs chicken tail boudin.">
                            <div class="foreseen">
                                <div class="stage"></div>
                                <span class="task-description">SR123456789 - Beef ribs chicken tail boudin.</span>
                            </div>
                        </div>                
                    </div>
                    <div id="task-week-6" class="week">
                        <div class="task start-day-7 interval-day-1">
                            <div class="foreseen">
                                <div class="stage"></div>
                                <span class="task-description">SR123456789 - Beef ribs chicken tail boudin.</span>
                            </div>
                        </div>
                    </div>
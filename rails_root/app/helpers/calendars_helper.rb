module CalendarsHelper
  def draw_plan(plan)
    result = %(<div class="calendar">)
    
    Date::DAYNAMES.each do |name|
      result << %(<div class="date header">#{name}</div>)
    end
    
    plan.executions.first.date.wday.times do
      result << %(<div class="date padding"></div>)
    end
    
    plan.executions.each do |execution|
      result << %(<div class="date #{execution.status} #{'actable' if execution.actable?}") 
      result << %(onclick="#{remote_function(
          :update => 'execution_detail', 
          :url => execution_url(execution), 
          :method => 'GET')}") if execution.actable?
      result << %(>&nbsp;<br />#{execution.date.day}</div>)
    end
    result += %(</div>)
    
    return result.html_safe
  end
end

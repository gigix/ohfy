module CalendarsHelper
  def draw_plan(plan, execution_drawer = web_execution_drawer)
    result = %(<div class='ohfy_calendar'>)
    
    Date::DAYNAMES.each do |name|
      result << %(<div class='date header'>#{name[0...3]}</div>)
    end
    
    plan.executions.first.date.wday.times do
      result << %(<div class='date padding'></div>)
    end
    
    plan.executions.each do |execution|
      execution_drawer.call(execution, result)
    end
    result += %(</div>)
    
    return result.html_safe
  end   
  
  def draw_plan_in_widget(plan)
    widget_execution_drawer = Proc.new do |execution, result|
      result << %(<div id='ohfy-execution-#{execution.date}' class='date #{execution.status}'>&nbsp;<br/>#{execution.date.day}</div>)
    end
    draw_plan(plan, widget_execution_drawer)
  end
  
  private
  def web_execution_drawer
    Proc.new do |execution, result|
      result << %(<div class='date #{execution.status} #{'actable' if execution.actable?}' id='execution-#{execution.date}') 
      result << %(onclick="#{remote_function(
          :update => 'execution_detail', 
          :url => execution_url(execution), 
          :method => 'GET')}") if execution.actable?
      result << %(><div class='month_title'>#{month_title(execution.date)}</div>#{execution.date.day}</div>)
    end
  end
  
  def month_title(date)
    return "&nbsp;" unless date.day == 1
    return Date::MONTHNAMES[date.month][0...3]
  end
end

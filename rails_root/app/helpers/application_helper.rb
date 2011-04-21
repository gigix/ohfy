module ApplicationHelper
  def message(msg)
    content_for :message, msg
  end
  
  def current_plan
    current_user.current_plan
  end
end

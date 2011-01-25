module ApplicationHelper
  def message(msg)
    content_for :message, msg
  end
end

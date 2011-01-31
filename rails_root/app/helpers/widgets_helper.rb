module WidgetsHelper
  def full_stylesheet_path(name)
    request.protocol + request.server_name + ":" + request.port.to_s + stylesheet_path(name)
  end
end

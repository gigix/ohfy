module WidgetsHelper
  def full_stylesheet_path(name)
    full_asset_path(stylesheet_path(name))
  end
  
  def full_image_path(name)
    full_asset_path(image_path(name))
  end

  def full_asset_path(asset_path)
    request.protocol + request.server_name + ":" + request.port.to_s + asset_path
  end
end

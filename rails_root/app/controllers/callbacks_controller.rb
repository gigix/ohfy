class CallbacksController < ApplicationController
  before_filter :authenticate_user!
  
  def sina
    client = current_user.sina_oauth_client
    client.authorize(:oauth_verifier => params[:oauth_verifier])
    current_user.set_sina_oauth_client!(client)
    redirect_to root_path
  end
end

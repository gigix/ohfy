class ExecutionsController < ApplicationController
  before_filter :authenticate_user!

  def show
    @execution = Execution.find(params[:id])
    render :layout => nil
  end
end

class ExecutionsController < ApplicationController
  def show
    @execution = Execution.find(params[:id])
    render :layout => nil
  end
end

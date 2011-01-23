Ohfy::Application.routes.draw do
  root :controller => :calendars, :action => :show
  
  devise_for :users
  
  resource :calendar, :as => :calendar
end

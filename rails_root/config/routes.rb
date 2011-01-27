Ohfy::Application.routes.draw do
  root :controller => :calendars, :action => :show
  
  devise_for :users
  
  resource :calendar, :as => :calendar
  resources :plans
  
  resources :executions do
    resources :activities
  end
end

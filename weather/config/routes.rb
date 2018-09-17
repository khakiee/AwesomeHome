Rails.application.routes.draw do
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
  resources :posts do
    collection do
      get :calendar_api_test
    end
  end
  get '/redirect', to: 'posts#redirect', as: 'redirect'
  get '/callback', to: 'posts#callback', as: 'callback'
  get '/calendars', to: 'posts#calendars', as: 'calendars'
  get '/test', to: 'posts#test',as: 'test'
end

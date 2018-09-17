class PostsController < ApplicationController

  # def initialize_calendar_api
  #
  # end
  #
  # def calendar_api_test
  #   CalendarApiService.new
  # end
  def index
    #code
  end
  def redirect
    client = Signet::OAuth2::Client.new(client_options)

    redirect_to client.authorization_uri.to_s
  end
  def callback
    client = Signet::OAuth2::Client.new(client_options)
    client.code = params[:code]

    response = client.fetch_access_token!

    session[:authorization] = response

    redirect_to calendars_url
  end

  def test
    now = Time.now
    @today_events = CalendarDatum.where("starts_at <= ?", Time.now.beginning_of_day + 9 * 3600).where("expires_at >= ?", Time.now.end_of_day + 9 * 3600)
    # @today_events=[]
  end

  def calendars
    client = Signet::OAuth2::Client.new(client_options)
    client.update!(session[:authorization])

    service = Google::Apis::CalendarV3::CalendarService.new
    service.authorization = client

    # @calendar_list = service.list_calendar_lists
    event_list = service.list_events("eric967712345@gmail.com")
    event_list.items.each do |event|
      start_time = Time.parse((event.start.try(:date_time) ? event.start.try(:date_time) : event.start.try(:date)).to_s).strftime("%Y/%m/%d")
      end_time = Time.parse((event.end.try(:date_time) ? event.end.try(:date_time) : event.end.try(:date)).to_s).strftime("%Y/%m/%d")
      CalendarDatum.where(title: event.summary, starts_at: start_time, expires_at: end_time).first_or_create
    end


    head :ok

  rescue Google::Apis::AuthorizationError
    response = client.refresh!

    session[:authorization] = session[:authorization].merge(response)

    retry
  end

  private

  def client_options
    {
      client_id: Rails.application.secrets.google_client_id,
      client_secret: Rails.application.secrets.google_client_secret,
      authorization_uri: 'https://accounts.google.com/o/oauth2/auth',
      token_credential_uri: 'https://www.googleapis.com/oauth2/v3/token',
      scope: Google::Apis::CalendarV3::AUTH_CALENDAR,
      redirect_uri: callback_url
    }
  end

end

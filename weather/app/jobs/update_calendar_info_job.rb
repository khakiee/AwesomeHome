class UpdateCalendarInfoJob < ApplicationJob
  queue_as :default
  RUN_EVERY = 1.minutes

  def perform(*args)
    
    self.class.perform_later(wait: RUN_EVERY)
  end
end

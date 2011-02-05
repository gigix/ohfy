Then /^I can see widget in container "(.*)"$/ do |widget_container|  
  wait_for_widget
  
  on_page_with :widget do |page|
    page.document.css(widget_container).length.should ==  1
    link = page.document.css('.ohfy-widget a').first
    link.attr('href').should == "#{Capybara.app_host}/"
    link.attr('target').should == '_blank'
  end
end

def wait_for_widget
  (1..5).each do
    break if page.has_css?(".ohfy-widget")
    sleep 1
  end
end
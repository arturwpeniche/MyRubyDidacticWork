require 'test_helper'

class WelcomeControllerTest < ActionController::TestCase
  test "should get costumer" do
    get :costumer
    assert_response :success
  end

end

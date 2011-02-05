module PageWithWidget
  include Gizmo::PageMixin
  
  def valid?
    @url =~ /\/widgets/
  end    
end
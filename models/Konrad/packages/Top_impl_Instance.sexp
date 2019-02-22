(ComponentInstance
  (name
    Top_impl_Instance)
  (classifier
    Top.impl)
  (features
    (feature
      transfer_switch
      (direction
        in)
      (classifier
        Boolean)))
  (subcomponents
    (ComponentInstance
      (name
        left_side)
      (classifier
        Side.impl)
      (features
        (feature
          other_side_pilot_flying
          (direction
            in)
          (classifier
            Boolean))
        (feature
          transfer_switch
          (direction
            in)
          (classifier
            Boolean))
        (feature
          pilot_flying_side
          (direction
            out)
          (classifier
            Boolean)))
      (subcomponents)
      (connections))
    (ComponentInstance
      (name
        right_side)
      (classifier
        Side.impl)
      (features
        (feature
          other_side_pilot_flying
          (direction
            in)
          (classifier
            Boolean))
        (feature
          transfer_switch
          (direction
            in)
          (classifier
            Boolean))
        (feature
          pilot_flying_side
          (direction
            out)
          (classifier
            Boolean)))
      (subcomponents)
      (connections))
    (ComponentInstance
      (name
        left_right)
      (classifier
        Channel.impl)
      (features
        (feature
          input
          (direction
            in)
          (classifier
            Boolean))
        (feature
          output
          (direction
            out)
          (classifier
            Boolean)))
      (subcomponents)
      (connections))
    (ComponentInstance
      (name
        right_left)
      (classifier
        Channel.impl)
      (features
        (feature
          input
          (direction
            in)
          (classifier
            Boolean))
        (feature
          output
          (direction
            out)
          (classifier
            Boolean)))
      (subcomponents)
      (connections)))
  (connections
    (connection
      c1
      left_side.pilot_flying_side
      left_right.input)
    (connection
      c2
      left_right.output
      right_side.other_side_pilot_flying)
    (connection
      c3
      right_side.pilot_flying_side
      right_left.input)
    (connection
      c4
      right_left.output
      left_side.other_side_pilot_flying)
    (connection
      c5
      transfer_switch
      left_side.transfer_switch)
    (connection
      c6
      transfer_switch
      right_side.transfer_switch)))
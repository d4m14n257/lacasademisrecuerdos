package com.client.service_client.model.record;

import com.client.service_client.model.File;
import com.client.service_client.model.Room;

public record RequestRoom(Room room, File file) { }

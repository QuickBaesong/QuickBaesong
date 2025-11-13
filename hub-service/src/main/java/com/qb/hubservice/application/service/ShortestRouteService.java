package com.qb.hubservice.application.service;

import com.qb.hubservice.domain.model.HubRoute;
import com.qb.hubservice.domain.repository.HubRouteRepository;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;
@NoArgsConstructor
public class ShortestRouteService {
    private final HubRouteRepository hubRouteRepository;

    /**
     * 두 허브 사이의 최단 경로를 다익스트라 알고리즘으로 계산
     */
    public ShortestPathResult findShortestPath(UUID startHubId, UUID endHubId) {
        // 그래프 구축
        Map<UUID, List<Edge>> graph = buildGraph();

        // 다익스트라 알고리즘 실행
        return dijkstra(graph, startHubId, endHubId);
    }

    /**
     * 그래프 구축 (HubRoute에서 간선 정보 추출)
     */
    private Map<UUID, List<Edge>> buildGraph() {
        Map<UUID, List<Edge>> graph = new HashMap<>();
        List<HubRoute> allRoutes = hubRouteRepository.findAll();

        for (HubRoute route : allRoutes) {
            UUID startHub = route.getStartHub().getHubId();
            UUID destHub = route.getDestinationHub().getHubId();

            Edge edge = new Edge(destHub, route.getDistance(), route.getDuration());

            // 양방향 그래프 (필요시 단방향으로 변경)
            graph.computeIfAbsent(startHub, k -> new ArrayList<>()).add(edge);

            // 역방향도 추가 (양방향 경로 지원)
            Edge reverseEdge = new Edge(startHub, route.getDistance(), route.getDuration());
            graph.computeIfAbsent(destHub, k -> new ArrayList<>()).add(reverseEdge);
        }

        return graph;
    }

    /**
     * 다익스트라 알고리즘
     */
    private ShortestPathResult dijkstra(Map<UUID, List<Edge>> graph, UUID start, UUID end) {
        Map<UUID, BigDecimal> distances = new HashMap<>();
        Map<UUID, UUID> previous = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparing(Node::getDistance));
        Set<UUID> visited = new HashSet<>();

        // 초기화
        for (UUID node : graph.keySet()) {
            distances.put(node, BigDecimal.valueOf(Double.MAX_VALUE));
        }
        distances.put(start, BigDecimal.ZERO);
        pq.offer(new Node(start, BigDecimal.ZERO));

        // 다익스트라 알고리즘
        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (visited.contains(current.getId())) {
                continue;
            }
            visited.add(current.getId());

            if (current.getId().equals(end)) {
                break;
            }

            List<Edge> neighbors = graph.getOrDefault(current.getId(), new ArrayList<>());
            for (Edge edge : neighbors) {
                UUID neighbor = edge.getDestinationHubId();
                BigDecimal newDistance = current.getDistance().add(edge.getDistance());

                if (newDistance.compareTo(distances.get(neighbor)) < 0) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, current.getId());
                    pq.offer(new Node(neighbor, newDistance));
                }
            }
        }

        // 경로 재구성
        List<UUID> path = new ArrayList<>();
        UUID current = end;

        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        if (path.isEmpty() || !path.get(0).equals(start)) {
            log.warn("경로를 찾을 수 없습니다. Start: {}, End: {}", start, end);
            return ShortestPathResult.notFound();
        }

        return ShortestPathResult.success(path, distances.get(end));
    }
}

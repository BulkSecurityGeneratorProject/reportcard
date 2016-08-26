(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('est-dispense', {
            parent: 'entity',
            url: '/est-dispense?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'EstDispenses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/est-dispense/est-dispenses.html',
                    controller: 'EstDispenseController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('est-dispense-detail', {
            parent: 'entity',
            url: '/est-dispense/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'EstDispense'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/est-dispense/est-dispense-detail.html',
                    controller: 'EstDispenseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'EstDispense', function($stateParams, EstDispense) {
                    return EstDispense.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'est-dispense',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('est-dispense-detail.edit', {
            parent: 'est-dispense-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/est-dispense/est-dispense-dialog.html',
                    controller: 'EstDispenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EstDispense', function(EstDispense) {
                            return EstDispense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('est-dispense.new', {
            parent: 'est-dispense',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/est-dispense/est-dispense-dialog.html',
                    controller: 'EstDispenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                annee: null,
                                coefficient: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('est-dispense', null, { reload: 'est-dispense' });
                }, function() {
                    $state.go('est-dispense');
                });
            }]
        })
        .state('est-dispense.edit', {
            parent: 'est-dispense',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/est-dispense/est-dispense-dialog.html',
                    controller: 'EstDispenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EstDispense', function(EstDispense) {
                            return EstDispense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('est-dispense', null, { reload: 'est-dispense' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('est-dispense.delete', {
            parent: 'est-dispense',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/est-dispense/est-dispense-delete-dialog.html',
                    controller: 'EstDispenseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EstDispense', function(EstDispense) {
                            return EstDispense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('est-dispense', null, { reload: 'est-dispense' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
